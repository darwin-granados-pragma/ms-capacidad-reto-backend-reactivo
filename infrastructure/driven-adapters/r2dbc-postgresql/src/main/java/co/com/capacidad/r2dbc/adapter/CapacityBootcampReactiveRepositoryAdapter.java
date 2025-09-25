package co.com.capacidad.r2dbc.adapter;

import co.com.capacidad.model.capacity.bootcamp.CapacityBootcamp;
import co.com.capacidad.model.gateways.CapacityBootcampRepository;
import co.com.capacidad.r2dbc.entity.CapacityBootcampEntity;
import co.com.capacidad.r2dbc.helper.ReactiveAdapterOperations;
import co.com.capacidad.r2dbc.repository.CapacityBootcampReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class CapacityBootcampReactiveRepositoryAdapter extends
    ReactiveAdapterOperations<CapacityBootcamp, CapacityBootcampEntity, String, CapacityBootcampReactiveRepository> implements
    CapacityBootcampRepository {

  public CapacityBootcampReactiveRepositoryAdapter(CapacityBootcampReactiveRepository repository,
      ObjectMapper mapper) {
    super(repository, mapper, d -> mapper.map(d, CapacityBootcamp.class));
  }

  @Override
  public Mono<CapacityBootcamp> save(CapacityBootcamp data) {
    log.info("Saving Capacity-Bootcamp");
    return super
        .save(data)
        .doOnSuccess(capacityBootcamp -> log.debug("Capacity-Bootcamp saved: {}",
            capacityBootcamp
        ));
  }
}
