package co.com.capacidad.usecase.capacity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import co.com.capacidad.model.capacity.Capacity;
import co.com.capacidad.model.capacity.CapacityResponse;
import co.com.capacidad.model.capacity.CapacitySortBy;
import co.com.capacidad.model.gateways.CapacityRepository;
import co.com.capacidad.model.gateways.TechnologyGateway;
import co.com.capacidad.model.page.CapacityPageCommand;
import co.com.capacidad.model.page.PageResponse;
import co.com.capacidad.model.page.SortDirection;
import co.com.capacidad.model.technology.Technology;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CapacitySortByNameUseCaseTest {

  @Mock
  private CapacityRepository repository;

  @Mock
  private TechnologyGateway technologyGateway;

  @InjectMocks
  private CapacitySortByNameUseCase capacitySortByNameUseCase;

  private CapacityPageCommand command;
  private Capacity capacity1;
  private Capacity capacity2;
  private Technology tech1;

  @BeforeEach
  void setUp() {
    command = CapacityPageCommand
        .builder()
        .page(0)
        .size(10)
        .sortBy(CapacitySortBy.NAME)
        .sortDirection(SortDirection.ASC)
        .build();

    capacity1 = Capacity
        .builder()
        .name("test name 1")
        .description("test description 1")
        .build();
    capacity2 = Capacity
        .builder()
        .name("test name 2")
        .description("test description 2")
        .build();

    tech1 = Technology
        .builder()
        .id("test id")
        .name("technology test name")
        .build();
  }

  @Test
  void shouldReturnTypeCorrectly() {
    // Assert
    assertEquals(CapacitySortBy.NAME, capacitySortByNameUseCase.getType());
  }

  @Test
  void shouldReturnPaginatedResponseWhenCapacitiesExist() {
    // Arrange
    when(repository.getTotalCount()).thenReturn(Mono.just(2L));
    when(repository.findAllOrderByName(command)).thenReturn(Flux.just(capacity1, capacity2));
    when(technologyGateway.getTechnologiesByCapacityId(capacity1.getId())).thenReturn(Flux.just(
        tech1));
    when(technologyGateway.getTechnologiesByCapacityId(capacity2.getId())).thenReturn(Flux.empty());

    // Act
    Mono<PageResponse<CapacityResponse>> result = capacitySortByNameUseCase.getCapacityResponse(
        command);

    // Assert
    StepVerifier
        .create(result)
        .expectNextMatches(pageResponse -> pageResponse.getTotalElements() == 2)
        .verifyComplete();
  }

  @Test
  void shouldReturnEmptyPageResponseWhenNoCapacitiesExist() {
    // Arrange
    when(repository.getTotalCount()).thenReturn(Mono.just(0L));
    when(repository.findAllOrderByName(command)).thenReturn(Flux.empty());

    // Act
    Mono<PageResponse<CapacityResponse>> result = capacitySortByNameUseCase.getCapacityResponse(
        command);

    // Assert
    StepVerifier
        .create(result)
        .expectNextMatches(pageResponse -> pageResponse.getTotalElements() == 0
            && pageResponse.getTotalPages() == 0 && pageResponse
            .getContent()
            .isEmpty())
        .verifyComplete();
  }
}