package co.com.capacidad.usecase.capacity;

import co.com.capacidad.model.capacity.Capacity;
import co.com.capacidad.model.capacity.CapacityCreate;
import co.com.capacidad.model.capacity.CapacityResponse;
import co.com.capacidad.model.capacity.CapacitySortBy;
import co.com.capacidad.model.error.ErrorCode;
import co.com.capacidad.model.exception.BusinessException;
import co.com.capacidad.model.exception.ObjectNotFoundException;
import co.com.capacidad.model.gateways.CapacityRepository;
import co.com.capacidad.model.gateways.TechnologyGateway;
import co.com.capacidad.model.gateways.TransactionalGateway;
import co.com.capacidad.model.input.CapacityRetrieveStrategy;
import co.com.capacidad.model.page.CapacityPageCommand;
import co.com.capacidad.model.page.PageResponse;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CapacityUseCase {

  private final CapacityRepository repository;
  private final TechnologyGateway technologyGateway;
  private final TransactionalGateway transactionalGateway;
  private final CapacityFactoryUseCase factoryUseCase;

  public Mono<Capacity> createCapacity(CapacityCreate data) {
    return validateTechnologiesSize(data.idTechnologies())
        .then(validateIdTechnologies(data.idTechnologies()))
        .then(buildAndSave(data))
        .flatMap(capacity -> technologyGateway
            .assignTechnologyToCapacity(capacity.getId(), data.idTechnologies())
            .thenReturn(capacity))
        .as(transactionalGateway::execute);
  }

  public Mono<PageResponse<CapacityResponse>> getCapacities(CapacityPageCommand command) {
    return Mono.defer(() -> {
      CapacitySortBy sortBy = command.getSortBy();
      CapacityRetrieveStrategy strategy = factoryUseCase.findStrategy(sortBy);
      return strategy.getCapacityResponse(command);
    });
  }

  public Flux<CapacityResponse> findCapacitiesByIdBootcamp(String idBootcamp) {
    return repository
        .findCapacitiesByIdBootcamp(idBootcamp)
        .flatMap(capacity -> technologyGateway
            .getTechnologiesByCapacityId(capacity.getId())
            .collectList()
            .map(technologyList -> CapacityResponse
                .builder()
                .id(capacity.getId())
                .name(capacity.getName())
                .description(capacity.getDescription())
                .technologies(technologyList)
                .build()))
        .switchIfEmpty(Mono.error(new ObjectNotFoundException(ErrorCode.BOOTCAMP_NOT_FOUND,
            idBootcamp
        )));
  }

  public Mono<Void> validateCapacities(Set<String> capacities) {
    return Flux
        .fromIterable(capacities)
        .flatMap(this::validateCapacityById)
        .then();
  }

  private Mono<Void> validateTechnologiesSize(Set<String> technologies) {
    if (technologies.size() < 3 || technologies.size() > 20) {
      return Mono.error(new BusinessException(ErrorCode.CAPACITY_TECHNOLOGIES_SIZE));
    }
    return Mono.empty();
  }

  private Mono<Void> validateIdTechnologies(Set<String> technologies) {
    return Mono.defer(() -> technologyGateway.validateTechnologies(technologies));
  }

  private Mono<Capacity> buildAndSave(CapacityCreate data) {
    return Mono
        .fromCallable(() -> Capacity
            .builder()
            .id(UUID
                .randomUUID()
                .toString())
            .name(data.name())
            .description(data.description())
            .isNew(true)
            .build())
        .flatMap(repository::save);
  }

  private Mono<Void> validateCapacityById(String id) {
    return repository
        .existsById(id)
        .flatMap(exists -> Boolean.TRUE.equals(exists) ? Mono.empty()
            : Mono.error(new ObjectNotFoundException(ErrorCode.CAPACITY_NOT_FOUND, id)));
  }
}
