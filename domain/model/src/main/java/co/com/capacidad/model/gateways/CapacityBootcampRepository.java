package co.com.capacidad.model.gateways;

import co.com.capacidad.model.capacity.bootcamp.CapacityBootcamp;
import reactor.core.publisher.Mono;

public interface CapacityBootcampRepository {
  Mono<CapacityBootcamp> save(CapacityBootcamp data);
}
