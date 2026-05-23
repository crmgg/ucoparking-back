package co.edu.uco.ucoparking.application.inputport;

import reactor.core.publisher.Mono;

public interface InputPort<T, R> {

    Mono<R> execute(T data);
}
