package co.com.capacidad.usecase.capacity.bootcamp;

import co.com.capacidad.model.capacity.CapacityTechnologyTotal;
import co.com.capacidad.model.capacity.bootcamp.CapacityBootcamp;
import co.com.capacidad.model.capacity.bootcamp.CapacityBootcampCreate;
import co.com.capacidad.model.gateways.CapacityBootcampRepository;
import co.com.capacidad.model.gateways.TechnologyGateway;
import co.com.capacidad.model.gateways.TransactionalGateway;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CapacityBootcampUseCase {

  private final CapacityBootcampRepository repository;
  private final TransactionalGateway transactionalGateway;
  private final TechnologyGateway technologyGateway;

  public Mono<Void> assignCapacitiesToBootcamp(CapacityBootcampCreate data) {
    return transactionalGateway.execute(getAssociationsToSave(data).then());
  }

  public Mono<CapacityTechnologyTotal> getCapacitiesAndTechnologiesByIdBootcamp(String idBootcamp) {
    Flux<CapacityBootcamp> capacitiesFlux = repository
        .findAllByIdBootcamp(idBootcamp)
        .cache();

    Mono<Long> totalCapacities = capacitiesFlux.count();

    Mono<Long> totalTechnologies = capacitiesFlux
        .flatMap(capacity -> technologyGateway.countTechnologiesByCapacityId(capacity.getIdCapacity()))
        .reduce(0L, Long::sum)
        .defaultIfEmpty(0L);

    return Mono.zip(totalCapacities, totalTechnologies, CapacityTechnologyTotal::new);
  }

  private Flux<CapacityBootcamp> getAssociationsToSave(CapacityBootcampCreate data) {
    return Flux
        .fromIterable(data.capacities())
        .map(idCapacity -> CapacityBootcamp
            .builder()
            .id(UUID
                .randomUUID()
                .toString())
            .idBootcamp(data.idBootcamp())
            .idCapacity(idCapacity)
            .isNew(true)
            .build())
        .flatMap(repository::save);
  }
}
