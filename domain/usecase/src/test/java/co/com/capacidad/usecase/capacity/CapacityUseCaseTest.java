package co.com.capacidad.usecase.capacity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.com.capacidad.model.capacity.Capacity;
import co.com.capacidad.model.capacity.CapacityCreate;
import co.com.capacidad.model.capacity.CapacityResponse;
import co.com.capacidad.model.capacity.CapacitySortBy;
import co.com.capacidad.model.capacity.bootcamp.CapacityBootcamp;
import co.com.capacidad.model.exception.BusinessException;
import co.com.capacidad.model.exception.ObjectNotFoundException;
import co.com.capacidad.model.gateways.CapacityBootcampRepository;
import co.com.capacidad.model.gateways.CapacityRepository;
import co.com.capacidad.model.gateways.TechnologyGateway;
import co.com.capacidad.model.gateways.TransactionalGateway;
import co.com.capacidad.model.input.CapacityRetrieveStrategy;
import co.com.capacidad.model.page.CapacityPageCommand;
import co.com.capacidad.model.page.PageResponse;
import co.com.capacidad.model.page.SortDirection;
import co.com.capacidad.model.technology.Technology;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CapacityUseCaseTest {

  @Mock
  private CapacityRepository repository;
  @Mock
  private TechnologyGateway technologyGateway;
  @Mock
  private TransactionalGateway transactionalGateway;
  @Mock
  private CapacityFactoryUseCase factoryUseCase;
  @Mock
  private CapacityRetrieveStrategy mockStrategy;
  @Mock
  private CapacityBootcampRepository capacityBootcampRepository;

  @InjectMocks
  private CapacityUseCase capacityUseCase;

  private Capacity capacity;
  private String idCapacity;
  private String idBootcamp;
  private Technology technology;

  private static Stream<Arguments> invalidTechnologySetsProvider() {
    Set<String> lessThanThree = Set.of("tech1", "tech2");
    Set<String> moreThanTwenty = IntStream
        .rangeClosed(1, 21)
        .mapToObj(i -> "tech" + i)
        .collect(Collectors.toSet());

    return Stream.of(Arguments.of(lessThanThree), Arguments.of(moreThanTwenty));
  }

  @BeforeEach
  void setUp() {
    capacity = Capacity
        .builder()
        .id("cap123")
        .name("DevOps")
        .description("CI/CD expert")
        .build();
    idCapacity = capacity.getId();
    idBootcamp = "test bootcamp id";
    technology = Technology
        .builder()
        .id("tech id")
        .name("tech name")
        .build();
  }

  @Test
  void shouldCreateCapacityAndAssignTechnologies() {
    // Arrange
    var techIds = Set.of("tech1", "tech2", "tech3");
    var createData = new CapacityCreate("DevOps", "CI/CD expert", techIds);
    when(technologyGateway.validateTechnologies(techIds)).thenReturn(Mono.empty());
    when(repository.save(any(Capacity.class))).thenReturn(Mono.just(capacity));
    when(technologyGateway.assignTechnologyToCapacity("cap123", techIds)).thenReturn(Mono.empty());
    when(transactionalGateway.execute(ArgumentMatchers.<Mono<?>>any())).thenAnswer(invocation -> invocation.getArgument(
        0));

    // Act
    Mono<Capacity> resultMono = capacityUseCase.createCapacity(createData);

    // Assert
    StepVerifier
        .create(resultMono)
        .expectNext(capacity)
        .verifyComplete();

    verify(technologyGateway).validateTechnologies(techIds);
    verify(repository).save(any(Capacity.class));
    verify(technologyGateway).assignTechnologyToCapacity("cap123", techIds);
    verify(transactionalGateway).execute(any());
  }

  @ParameterizedTest
  @MethodSource("invalidTechnologySetsProvider")
  void shouldReturnErrorWhenTechnologySizeIsInvalid(Set<String> invalidTechIds) {
    // Arrange
    var createData = new CapacityCreate("Invalid", "Invalid", invalidTechIds);
    when(transactionalGateway.execute(ArgumentMatchers.<Mono<?>>any())).thenAnswer(invocation -> invocation.getArgument(
        0));

    // Act
    Mono<Capacity> resultMono = capacityUseCase.createCapacity(createData);

    // Assert
    StepVerifier
        .create(resultMono)
        .expectError(BusinessException.class)
        .verify();
    verify(repository, never()).save(any());
    verify(technologyGateway, never()).assignTechnologyToCapacity(any(), any());
  }

  @Test
  void shouldFindAndExecuteStrategySuccessfully() {
    // Arrange
    CapacityPageCommand command = CapacityPageCommand
        .builder()
        .page(0)
        .size(10)
        .sortBy(CapacitySortBy.NAME)
        .sortDirection(SortDirection.ASC)
        .build();
    PageResponse<CapacityResponse> expectedResponse = PageResponse
        .<CapacityResponse>builder()
        .content(Collections.emptyList())
        .totalElements(0)
        .build();
    when(factoryUseCase.findStrategy(CapacitySortBy.NAME)).thenReturn(mockStrategy);
    when(mockStrategy.getCapacityResponse(command)).thenReturn(Mono.just(expectedResponse));

    // Act
    var result = capacityUseCase.getCapacities(command);

    // Assert
    StepVerifier
        .create(result)
        .expectNext(expectedResponse)
        .verifyComplete();
  }

  @Test
  void shouldCompleteWhenAllIdsExist() {
    // Arrange
    Set<String> existingIds = Set.of("id-1", "id-2", "id-3");
    when(repository.existsById(anyString())).thenReturn(Mono.just(true));

    // Act
    var result = capacityUseCase.validateCapacities(existingIds);

    // Assert
    StepVerifier
        .create(result)
        .verifyComplete();
  }

  @Test
  void shouldReturnErrorWhenOneIdDoesNotExist() {
    // Arrange
    String invalidId = "invalid-id";
    String validId1 = "id-1";
    String validId3 = "id-3";
    Set<String> idsToValidate = Set.of(validId1, invalidId, validId3);

    lenient()
        .when(repository.existsById(validId1))
        .thenReturn(Mono.just(true));
    lenient()
        .when(repository.existsById(validId3))
        .thenReturn(Mono.just(true));
    when(repository.existsById(invalidId)).thenReturn(Mono.just(false));

    // Act
    var result = capacityUseCase.validateCapacities(idsToValidate);

    // Assert
    StepVerifier
        .create(result)
        .expectError(ObjectNotFoundException.class)
        .verify();
  }

  @Test
  void shouldReturnCapacitiesByIdBootcampSuccessfully() {
    // Arrange
    when(repository.findCapacitiesByIdBootcamp(idBootcamp)).thenReturn(Flux.just(capacity));
    when(technologyGateway.getTechnologiesByCapacityId(idCapacity)).thenReturn(Flux.just(technology));

    // Act
    var result = capacityUseCase.findCapacitiesByIdBootcamp(idBootcamp);

    // Assert
    StepVerifier
        .create(result)
        .expectNextMatches(capacityResponse -> capacityResponse
            .getName()
            .equals(capacity.getName()) && capacityResponse
            .getTechnologies()
            .size() == 1)
        .verifyComplete();
  }

  @Test
  void shouldThrowObjectNotFoundExceptionWhenBootcampNotFound() {
    // Arrange
    when(repository.findCapacitiesByIdBootcamp(idBootcamp)).thenReturn(Flux.empty());

    // Act
    var result = capacityUseCase.findCapacitiesByIdBootcamp(idBootcamp);

    // Assert
    StepVerifier
        .create(result)
        .expectError(ObjectNotFoundException.class)
        .verify();
  }

  @Test
  void delete_whenBootcampHasCapacities_shouldDeleteAllRelations() {
    // Arrange
    var capacityBootcamp1 = CapacityBootcamp
        .builder()
        .id("cb-1")
        .idCapacity("cap-1")
        .idBootcamp(idBootcamp)
        .build();
    var capacityBootcamp2 = CapacityBootcamp
        .builder()
        .id("cb-2")
        .idCapacity("cap-2")
        .idBootcamp(idBootcamp)
        .build();
    var capacities = List.of(capacityBootcamp1, capacityBootcamp2);

    when(capacityBootcampRepository.findAllByIdBootcamp(idBootcamp)).thenReturn(Flux.fromIterable(
        capacities));
    when(capacityBootcampRepository.deleteAll(capacities)).thenReturn(Mono.empty());
    when(repository.deleteById(anyString())).thenReturn(Mono.empty());
    when(technologyGateway.deleteRelationsCapacityTechnologies(anyString())).thenReturn(Mono.empty());

    when(transactionalGateway.execute(ArgumentMatchers.<Mono<?>>any())).thenAnswer(invocation -> invocation.getArgument(
        0));

    // Act
    var resultMono = capacityUseCase.deleteBootcampCapacitiesAndRelationsWithTechnologies(idBootcamp);

    // Assert
    StepVerifier
        .create(resultMono)
        .expectComplete()
        .verify();
    verify(capacityBootcampRepository, times(1)).deleteAll(capacities);
    verify(repository, times(1)).deleteById(capacityBootcamp1.getIdCapacity());
    verify(repository, times(1)).deleteById(capacityBootcamp2.getIdCapacity());
    verify(technologyGateway,
        times(1)
    ).deleteRelationsCapacityTechnologies(capacityBootcamp1.getIdCapacity());
    verify(technologyGateway,
        times(1)
    ).deleteRelationsCapacityTechnologies(capacityBootcamp2.getIdCapacity());
  }

  @Test
  void delete_whenBootcampHasNoCapacities_shouldCompleteWithoutDeletions() {
    // Arrange
    when(capacityBootcampRepository.findAllByIdBootcamp(idBootcamp)).thenReturn(Flux.empty());

    // Act
    var resultMono = capacityUseCase.deleteBootcampCapacitiesAndRelationsWithTechnologies(idBootcamp);

    // Assert
    StepVerifier
        .create(resultMono)
        .expectComplete()
        .verify();
    verify(capacityBootcampRepository, never()).deleteAll(any());
    verify(repository, never()).deleteById(anyString());
    verify(technologyGateway, never()).deleteRelationsCapacityTechnologies(anyString());
  }
}