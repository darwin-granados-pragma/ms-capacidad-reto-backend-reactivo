package co.com.capacidad.usecase.capacity.bootcamp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.com.capacidad.model.capacity.CapacityTechnologyTotal;
import co.com.capacidad.model.capacity.bootcamp.CapacityBootcamp;
import co.com.capacidad.model.capacity.bootcamp.CapacityBootcampCreate;
import co.com.capacidad.model.gateways.CapacityBootcampRepository;
import co.com.capacidad.model.gateways.TechnologyGateway;
import co.com.capacidad.model.gateways.TransactionalGateway;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CapacityBootcampUseCaseTest {

  CapacityBootcampCreate capacityBootcampCreate = new CapacityBootcampCreate("boot-01",
      Set.of("cap-1", "cap-2", "cap-3")
  );

  @Mock
  private CapacityBootcampRepository repository;
  @Mock
  private TransactionalGateway transactionalGateway;
  @Mock
  private TechnologyGateway technologyGateway;

  @InjectMocks
  private CapacityBootcampUseCase capacityBootcampUseCase;

  @Test
  void shouldSaveAllAssociations() {
    // Arrange
    when(repository.save(any(CapacityBootcamp.class))).thenReturn(Mono.just(new CapacityBootcamp()));
    when(transactionalGateway.execute(ArgumentMatchers.<Mono<?>>any())).thenAnswer(invocation -> invocation.getArgument(
        0));

    // Act
    Mono<Void> result = capacityBootcampUseCase.assignCapacitiesToBootcamp(capacityBootcampCreate);

    // Assert
    StepVerifier
        .create(result)
        .verifyComplete();
    verify(repository, times(3)).save(any(CapacityBootcamp.class));
    verify(transactionalGateway, times(1)).execute(any());
  }

  @Test
  void shouldGetCapacitiesAndTechnologiesTotal() {
    // Arrange
    String idBootcamp = "boot-01";
    CapacityBootcamp capacity1 = CapacityBootcamp
        .builder()
        .idCapacity("cap-1")
        .build();
    CapacityBootcamp capacity2 = CapacityBootcamp
        .builder()
        .idCapacity("cap-2")
        .build();

    when(repository.findAllByIdBootcamp(idBootcamp)).thenReturn(Flux.just(capacity1, capacity2));
    when(technologyGateway.countTechnologiesByCapacityId("cap-1")).thenReturn(Mono.just(5L));
    when(technologyGateway.countTechnologiesByCapacityId("cap-2")).thenReturn(Mono.just(10L));

    // Act
    Mono<CapacityTechnologyTotal> result = capacityBootcampUseCase.getCapacitiesAndTechnologiesByIdBootcamp(
        idBootcamp);

    // Assert
    StepVerifier
        .create(result)
        .expectNextMatches(total -> total
            .totalCapacity()
            .equals(2L) && total
            .totalTechnology()
            .equals(15L))
        .verifyComplete();

    verify(repository).findAllByIdBootcamp(idBootcamp);
    verify(technologyGateway).countTechnologiesByCapacityId("cap-1");
    verify(technologyGateway).countTechnologiesByCapacityId("cap-2");
  }

}