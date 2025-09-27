package co.com.capacidad.model.gateways;

import co.com.capacidad.model.capacity.bootcamp.CapacityBootcamp;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CapacityBootcampRepository {

  Mono<CapacityBootcamp> save(CapacityBootcamp data);

  Flux<CapacityBootcamp> findAllByIdBootcamp(String idBootcamp);

  Mono<Void> deleteAll(List<CapacityBootcamp> capacityBootcamps);
}
