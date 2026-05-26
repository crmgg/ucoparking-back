package co.edu.uco.ucoparking.infraestructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Devuelve 401 cuando el Bearer token falta, es inválido o no se puede decodificar (JWE/opaco).
 */
public class Auth0AuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(Auth0AuthenticationEntryPoint.class);

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        log.warn("Autenticación rechazada [{} {}]: {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getPath().value(),
                ex.getMessage());
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
