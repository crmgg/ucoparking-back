package co.edu.uco.ucoparking.infraestructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import reactor.core.publisher.Mono;

/**
 * Evita 500 cuando Auth0 envía JWE (5 partes) u opaco en lugar de access token JWS (3 partes).
 */
class SafeReactiveJwtDecoder implements ReactiveJwtDecoder {

    private static final Logger log = LoggerFactory.getLogger(SafeReactiveJwtDecoder.class);

    private final ReactiveJwtDecoder delegate;

    SafeReactiveJwtDecoder(ReactiveJwtDecoder delegate) {
        this.delegate = delegate;
    }

    @Override
    public Mono<Jwt> decode(String token) {
        int parts = token == null ? 0 : token.split("\\.").length;

        if (parts == 5) {
            return Mono.error(new JwtException(
                    "Token JWE (encriptado). Desactiva Access Token Encryption en Auth0 API uco-parking-api."));
        }

        if (parts != 3) {
            return Mono.error(new JwtException(
                    "Token no es JWT JWS (partes=" + parts + "). Pide access token con audience https://uco-parking-api."));
        }

        return delegate.decode(token)
                .onErrorMap(ClassCastException.class, ex -> {
                    log.warn("JWT ClassCastException (JWE vs JWS): {}", ex.getMessage());
                    return new JwtException(
                            "Token JWT inválido. Envía access token JWS con audience del API, no id_token ni JWE.", ex);
                });
    }
}
