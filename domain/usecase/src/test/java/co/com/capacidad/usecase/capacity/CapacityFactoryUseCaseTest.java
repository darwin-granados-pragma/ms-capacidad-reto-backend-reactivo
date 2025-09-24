package co.com.capacidad.usecase.capacity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import co.com.capacidad.model.capacity.CapacitySortBy;
import co.com.capacidad.model.exception.BusinessException;
import co.com.capacidad.model.input.CapacityRetrieveStrategy;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CapacityFactoryUseCaseTest {

  @Mock
  private CapacityRetrieveStrategy sortByNameStrategy;

  @Mock
  private CapacityRetrieveStrategy sortByTechCountStrategy;

  private CapacityFactoryUseCase capacityFactoryUseCase;

  @BeforeEach
  void setUp() {
    when(sortByNameStrategy.getType()).thenReturn(CapacitySortBy.NAME);
  }

  @Test
  void shouldReturnCorrectStrategyWhenExists() {
    // Arrange
    capacityFactoryUseCase = new CapacityFactoryUseCase(List.of(sortByNameStrategy,
        sortByTechCountStrategy
    ));

    // Act
    CapacityRetrieveStrategy foundStrategy = capacityFactoryUseCase.findStrategy(CapacitySortBy.NAME);

    // Assert
    assertNotNull(foundStrategy);
    assertEquals(CapacitySortBy.NAME, foundStrategy.getType());
    assertEquals(sortByNameStrategy, foundStrategy);
  }

  @Test
  void shouldThrowExceptionWhenStrategyNotFound() {
    // Arrange
    capacityFactoryUseCase = new CapacityFactoryUseCase(List.of(sortByNameStrategy));

    // Act & Assert
    assertThrows(BusinessException.class,
        () -> capacityFactoryUseCase.findStrategy(CapacitySortBy.TECH_COUNT)
    );
  }
}