package co.com.capacidad.model.gateways;

import reactor.core.publisher.Mono;

public interface TransactionalGateway {

  <T> Mono<T> execute(Mono<T> action);
}
