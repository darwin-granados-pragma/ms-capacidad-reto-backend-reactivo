package co.com.capacidad.r2dbc.repository;

import co.com.capacidad.r2dbc.entity.CapacityBootcampEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CapacityBootcampReactiveRepository extends
    ReactiveCrudRepository<CapacityBootcampEntity, String>,
    ReactiveQueryByExampleExecutor<CapacityBootcampEntity> {

  Flux<CapacityBootcampEntity> findAllByIdBootcamp(String idBootcamp);
}
