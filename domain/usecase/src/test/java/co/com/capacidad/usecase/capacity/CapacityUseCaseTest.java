package co.com.capacidad.usecase.capacity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.com.capacidad.model.capacity.Capacity;
import co.com.capacidad.model.capacity.CapacityCreate;
import co.com.capacidad.model.capacity.CapacityResponse;
import co.com.capacidad.model.capacity.CapacitySortBy;
import co.com.capacidad.model.exception.BusinessException;
import co.com.capacidad.model.exception.ObjectNotFoundException;
import co.com.capacidad.model.gateways.CapacityRepository;
import co.com.capacidad.model.gateways.TechnologyGateway;
import co.com.capacidad.model.gateways.TransactionalGateway;
import co.com.capacidad.model.input.CapacityRetrieveStrategy;
import co.com.capacidad.model.page.CapacityPageCommand;
import co.com.capacidad.model.page.PageResponse;
import co.com.capacidad.model.page.SortDirection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

  @InjectMocks
  private CapacityUseCase capacityUseCase;

  private static Stream<Arguments> invalidTechnologySetsProvider() {
    Set<String> lessThanThree = Set.of("tech1", "tech2");
    Set<String> moreThanTwenty = IntStream
        .rangeClosed(1, 21)
        .mapToObj(i -> "tech" + i)
        .collect(Collectors.toSet());

    return Stream.of(Arguments.of(lessThanThree), Arguments.of(moreThanTwenty));
  }

  @Test
  void shouldCreateCapacityAndAssignTechnologies() {
    // Arrange
    var techIds = Set.of("tech1", "tech2", "tech3");
    var createData = new CapacityCreate("DevOps", "CI/CD expert", techIds);
    var savedCapacity = Capacity
        .builder()
        .id("cap123")
        .name("DevOps")
        .description("CI/CD expert")
        .build();
    when(technologyGateway.validateTechnologies(techIds)).thenReturn(Mono.empty());
    when(repository.save(any(Capacity.class))).thenReturn(Mono.just(savedCapacity));
    when(technologyGateway.assignTechnologyToCapacity("cap123", techIds)).thenReturn(Mono.empty());
    when(transactionalGateway.execute(ArgumentMatchers.<Mono<?>>any())).thenAnswer(invocation -> invocation.getArgument(
        0));

    // Act
    Mono<Capacity> resultMono = capacityUseCase.createCapacity(createData);

    // Assert
    StepVerifier
        .create(resultMono)
        .expectNext(savedCapacity)
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
}