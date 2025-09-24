package co.com.capacidad.usecase.capacity;

import co.com.capacidad.model.capacity.Capacity;
import co.com.capacidad.model.capacity.CapacityResponse;
import co.com.capacidad.model.capacity.CapacitySortBy;
import co.com.capacidad.model.gateways.CapacityRepository;
import co.com.capacidad.model.gateways.TechnologyGateway;
import co.com.capacidad.model.input.CapacityRetrieveStrategy;
import co.com.capacidad.model.page.CapacityPageCommand;
import co.com.capacidad.model.page.PageResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CapacitySortByNameUseCase implements CapacityRetrieveStrategy {

  private final CapacityRepository repository;
  private final TechnologyGateway technologyGateway;

  @Override
  public CapacitySortBy getType() {
    return CapacitySortBy.NAME;
  }

  @Override
  public Mono<PageResponse<CapacityResponse>> getCapacityResponse(CapacityPageCommand command) {
    return repository
        .getTotalCount()
        .flatMap(total -> getLoans(command)
            .collectList()
            .flatMap(capacityResponses -> buildPageResponse(capacityResponses, command, total)));
  }

  private Flux<CapacityResponse> getLoans(CapacityPageCommand command) {
    return repository
        .findAllOrderByName(command)
        .flatMap(this::mapToCapacityResponse);
  }

  private Mono<CapacityResponse> mapToCapacityResponse(Capacity capacity) {
    return technologyGateway
        .getTechnologiesByCapacityId(capacity.getId())
        .collectList()
        .map(technologiesSet -> CapacityResponse
            .builder()
            .id(capacity.getId())
            .name(capacity.getName())
            .description(capacity.getDescription())
            .technologies(technologiesSet)
            .build());
  }

  private Mono<PageResponse<CapacityResponse>> buildPageResponse(List<CapacityResponse> content,
      CapacityPageCommand command, long totalElements) {
    return Mono.defer(() -> {
      int totalPages = (int) Math.ceil((double) totalElements / command.getSize());
      return Mono.just(PageResponse
          .<CapacityResponse>builder()
          .content(content)
          .page(command.getPage())
          .size(command.getSize())
          .totalElements(totalElements)
          .totalPages(totalPages)
          .build());
    });
  }
}
