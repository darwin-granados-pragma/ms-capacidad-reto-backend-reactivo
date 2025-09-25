package co.com.capacidad.r2dbc.repository;

import co.com.capacidad.r2dbc.entity.CapacityBootcampEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CapacityBootcampReactiveRepository extends
    ReactiveCrudRepository<CapacityBootcampEntity, String>,
    ReactiveQueryByExampleExecutor<CapacityBootcampEntity> {

}
