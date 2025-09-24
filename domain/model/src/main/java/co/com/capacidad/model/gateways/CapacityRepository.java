package co.com.capacidad.model.gateways;

import co.com.capacidad.model.capacity.Capacity;
import co.com.capacidad.model.page.CapacityPageCommand;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CapacityRepository {

  Mono<Capacity> save(Capacity capacity);

  Flux<Capacity> findAllOrderByName(CapacityPageCommand command);

  Mono<Boolean> existsById(String id);

  Mono<Long> getTotalCount();
}
