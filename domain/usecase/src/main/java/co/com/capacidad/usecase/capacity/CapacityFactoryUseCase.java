package co.com.capacidad.usecase.capacity;

import co.com.capacidad.model.capacity.CapacitySortBy;
import co.com.capacidad.model.error.ErrorCode;
import co.com.capacidad.model.exception.BusinessException;
import co.com.capacidad.model.input.CapacityRetrieveStrategy;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CapacityFactoryUseCase {

  private final Map<CapacitySortBy, CapacityRetrieveStrategy> strategyMap;

  public CapacityFactoryUseCase(List<CapacityRetrieveStrategy> strategies) {
    this.strategyMap = strategies.stream()
        .collect(Collectors.toMap(CapacityRetrieveStrategy::getType, Function.identity()));
  }

  public CapacityRetrieveStrategy findStrategy(CapacitySortBy type) {
    CapacityRetrieveStrategy strategy = strategyMap.get(type);
    if (strategy == null) {
      throw new BusinessException(ErrorCode.INVALID_SORT_BY);
    }
    return strategy;
  }
}
