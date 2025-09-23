package co.com.capacidad.transactional;

import co.com.capacidad.model.gateways.TransactionalGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionalGatewayAdapter implements TransactionalGateway {

  private final TransactionalOperator operator;

  @Override
  public <T> Mono<T> execute(Mono<T> action) {
    log.info("Executing transaction");
    return operator
        .transactional(action)
        .doOnSuccess(result -> log.info("Transaction completed successfully"))
        .doOnError(error -> log.error("Transaction failed", error));
  }
}
