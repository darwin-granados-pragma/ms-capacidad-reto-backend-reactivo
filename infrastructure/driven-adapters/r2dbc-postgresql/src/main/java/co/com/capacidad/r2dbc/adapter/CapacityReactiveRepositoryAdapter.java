package co.com.capacidad.r2dbc.adapter;

import co.com.capacidad.model.capacity.Capacity;
import co.com.capacidad.model.gateways.CapacityRepository;
import co.com.capacidad.model.page.CapacityPageCommand;
import co.com.capacidad.r2dbc.entity.CapacityEntity;
import co.com.capacidad.r2dbc.helper.ReactiveAdapterOperations;
import co.com.capacidad.r2dbc.repository.CapacityReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
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
        .doOnSuccess(capacitySaved -> log.debug("Capacity saved: {}", capacitySaved));
  }

  @Override
  public Flux<Capacity> findAllOrderByName(CapacityPageCommand command) {
    log.info("Retrieving capacities");
    return Flux.defer(() -> {
      Sort sort = Sort.by(Sort.Direction.fromString(command
          .getSortDirection()
          .name()), "name"
      );
      Pageable pageable = PageRequest.of(command.getPage(), command.getSize(), sort);

      return super.repository
          .findAllBy(pageable)
          .map(this::toEntity);
    });
  }

  @Override
  public Mono<Boolean> existsById(String id) {
    log.info("Validating existence of the capacity by id: {}", id);
    return super.repository.existsById(id);
  }

  @Override
  public Mono<Long> getTotalCount() {
    log.info("Getting total elements of the Capacity");
    return super.repository.count();
  }

  @Override
  public Flux<Capacity> findCapacitiesByIdBootcamp(String idBootcamp) {
    log.info("Retrieving capacities from bootcamp with id: {}", idBootcamp);
    return super.repository
        .findCapacitiesByIdBootcamp(idBootcamp)
        .map(this::toEntity);
  }
}
