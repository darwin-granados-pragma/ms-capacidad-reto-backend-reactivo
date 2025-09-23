package co.com.capacidad.usecase.capacity;

import co.com.capacidad.model.capacity.Capacity;
import co.com.capacidad.model.capacity.CapacityCreate;
import co.com.capacidad.model.error.ErrorCode;
import co.com.capacidad.model.exception.BusinessException;
import co.com.capacidad.model.gateways.CapacityRepository;
import co.com.capacidad.model.gateways.TechnologyGateway;
import co.com.capacidad.model.gateways.TransactionalGateway;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CapacityUseCase {

  private final CapacityRepository repository;
  private final TechnologyGateway technologyGateway;
  private final TransactionalGateway transactionalGateway;

  public Mono<Capacity> createCapacity(CapacityCreate data) {
    return validateTechnologiesSize(data.idTechnologies())
        .then(validateIdTechnologies(data.idTechnologies()))
        .then(buildAndSave(data))
        .flatMap(capacity -> technologyGateway
            .assignTechnologyToCapacity(capacity.getId(), data.idTechnologies())
            .thenReturn(capacity))
        .as(transactionalGateway::execute);
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
            .id(UUID.randomUUID().toString())
            .name(data.name())
            .description(data.description())
            .isNew(true)
            .build())
        .flatMap(repository::save);
  }

}
