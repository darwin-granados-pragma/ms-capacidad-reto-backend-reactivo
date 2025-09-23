package co.com.capacidad.r2dbc.repository;

import co.com.capacidad.r2dbc.entity.CapacityEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CapacityReactiveRepository extends ReactiveCrudRepository<CapacityEntity, String>,
    ReactiveQueryByExampleExecutor<CapacityEntity> {

  Flux<CapacityEntity> findAllBy(Pageable pageable);
}
