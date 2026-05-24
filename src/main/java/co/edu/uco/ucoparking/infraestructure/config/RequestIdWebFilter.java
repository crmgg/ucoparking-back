package co.edu.uco.ucoparking.infraestructure.config;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.UUID;

@Component
public class RequestIdWebFilter implements WebFilter {

    public static final String REQUEST_ID_HEADER = "X-Request-Id";
    public static final String REQUEST_ID_MDC_KEY = "requestId";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String requestId = exchange.getRequest().getHeaders().getFirst(REQUEST_ID_HEADER);
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }

        exchange.getResponse().getHeaders().add(REQUEST_ID_HEADER, requestId);
        String finalRequestId = requestId;

        return chain.filter(exchange)
                .contextWrite(Context.of(REQUEST_ID_MDC_KEY, finalRequestId))
                .doOnEach(signal -> {
                    if (signal.isOnNext() || signal.isOnComplete() || signal.isOnError()) {
                        MDC.put(REQUEST_ID_MDC_KEY, finalRequestId);
                    }
                })
                .doFinally(signalType -> MDC.remove(REQUEST_ID_MDC_KEY));
    }
}
