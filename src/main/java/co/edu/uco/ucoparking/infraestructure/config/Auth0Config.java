package co.edu.uco.ucoparking.infraestructure.config;



import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.http.HttpStatus;

import org.springframework.security.config.Customizer;

import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

import org.springframework.security.config.web.server.ServerHttpSecurity;

import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;

import org.springframework.security.oauth2.core.OAuth2TokenValidator;

import org.springframework.security.oauth2.jwt.Jwt;

import org.springframework.security.oauth2.jwt.JwtValidators;

import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;

import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import org.springframework.security.web.server.SecurityWebFilterChain;

import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;



@Configuration

@EnableWebFluxSecurity

public class Auth0Config {



    private static final Logger log = LoggerFactory.getLogger(Auth0Config.class);



    @Value("${auth0.security.enabled:false}")

    private boolean securityEnabled;



    @Value("${auth0.audience:}")

    private String audience;



    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri:}")

    private String issuerUri;



    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri:}")

    private String jwkSetUri;



    @Bean

    public Auth0AuthenticationEntryPoint auth0AuthenticationEntryPoint() {

        return new Auth0AuthenticationEntryPoint();

    }



    @Bean

    public SecurityWebFilterChain securityWebFilterChain(

            ServerHttpSecurity http,

            Auth0AuthenticationEntryPoint authenticationEntryPoint

    ) {

        http.csrf(ServerHttpSecurity.CsrfSpec::disable)

                .cors(Customizer.withDefaults());



        if (!securityEnabled) {

            return http.authorizeExchange(exchanges -> exchanges.anyExchange().permitAll())

                    .build();

        }



        ServerAuthenticationFailureHandler authFailureHandler = (webFilterExchange, ex) -> {

            log.warn("Bearer token inválido [{} {}]: {}",

                    webFilterExchange.getExchange().getRequest().getMethod(),

                    webFilterExchange.getExchange().getRequest().getPath().value(),

                    ex.getMessage());

            var response = webFilterExchange.getExchange().getResponse();

            response.setStatusCode(HttpStatus.UNAUTHORIZED);

            return response.setComplete();

        };



        return http.authorizeExchange(exchanges -> exchanges

                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .pathMatchers(

                                "/v3/api-docs/**",

                                "/swagger-ui/**",

                                "/swagger-ui.html",

                                "/actuator/**"

                        ).permitAll()

                        .anyExchange().authenticated()

                )

                .oauth2ResourceServer(oauth2 -> oauth2

                        .jwt(jwt -> jwt.jwtDecoder(reactiveJwtDecoder()))

                        .authenticationEntryPoint(authenticationEntryPoint)

                        .authenticationFailureHandler(authFailureHandler)

                )

                .exceptionHandling(ex -> ex

                        .authenticationEntryPoint(authenticationEntryPoint)

                        .accessDeniedHandler((exchange, denied) -> {

                            log.warn("Acceso denegado [{} {}]: {}",

                                    exchange.getRequest().getMethod(),

                                    exchange.getRequest().getPath().value(),

                                    denied.getMessage());

                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);

                            return exchange.getResponse().setComplete();

                        })

                )

                .build();

    }



    @Bean
    @ConditionalOnProperty(name = "auth0.security.enabled", havingValue = "true")
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        if (jwkSetUri == null || jwkSetUri.isBlank()) {
            throw new IllegalStateException(
                    "auth0.security.enabled=true pero falta spring.security.oauth2.resourceserver.jwt.jwk-set-uri");
        }

        String normalizedIssuer = issuerUri.endsWith("/") ? issuerUri : issuerUri + "/";



        log.info("Auth0 JWT decoder: issuer-uri={}, jwk-set-uri={}", normalizedIssuer, jwkSetUri);



        NimbusReactiveJwtDecoder decoder = NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();



        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(normalizedIssuer);



        if (audience != null && !audience.isBlank()) {

            OAuth2TokenValidator<Jwt> withAudience = new Auth0AudienceValidator(audience);

            decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(withIssuer, withAudience));

        } else {

            decoder.setJwtValidator(withIssuer);

        }



        return new SafeReactiveJwtDecoder(decoder);

    }

}

