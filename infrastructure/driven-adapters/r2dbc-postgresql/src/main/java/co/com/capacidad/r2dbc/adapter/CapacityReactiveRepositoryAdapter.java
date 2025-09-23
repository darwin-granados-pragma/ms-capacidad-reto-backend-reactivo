package co.com.capacidad.r2dbc.adapter;

import co.com.capacidad.model.capacity.Capacity;
import co.com.capacidad.model.error.ErrorCode;
import co.com.capacidad.model.exception.ConstraintException;
import co.com.capacidad.model.gateways.CapacityRepository;
import co.com.capacidad.r2dbc.entity.CapacityEntity;
import co.com.capacidad.r2dbc.helper.ReactiveAdapterOperations;
import co.com.capacidad.r2dbc.repository.CapacityReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class CapacityReactiveRepositoryAdapter extends
    ReactiveAdapterOperations<Capacity, CapacityEntity, String, CapacityReactiveRepository> implements
    CapacityRepository {

  public CapacityReactiveRepositoryAdapter(CapacityReactiveRepository repository,
      ObjectMapper mapper) {
    super(repository, mapper, d -> mapper.map(d, Capacity.class));
  }

  @Override
  public Mono<Capacity> save(Capacity capacity) {
    log.info("Saving capacity with name: {}", capacity.getName());
    return super
        .save(capacity)
        .onErrorMap(DataIntegrityViolationException.class, e -> {
              log.error("Data integrity violation: {}", e.getMessage());
              String message = e.getMessage();
              if (message.contains("nombre_unique_constraint")) {
                return new ConstraintException(ErrorCode.CAPACITY_NAME_ALREADY_EXISTS);
              }
              return new ConstraintException(ErrorCode.CONSTRAINT_VIOLATION);
            }
        )
        .doOnSuccess(capacitySaved -> log.debug("Capacity saved: {}", capacitySaved));
  }
}
