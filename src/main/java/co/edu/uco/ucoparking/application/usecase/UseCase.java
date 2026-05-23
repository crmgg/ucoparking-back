package co.edu.uco.ucoparking.application.usecase;

import reactor.core.publisher.Mono;

public interface UseCase <D, R> {

    Mono<R> execute(D data);
}
