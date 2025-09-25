package co.com.capacidad.model.capacity;

import co.com.capacidad.model.error.ErrorCode;
import co.com.capacidad.model.exception.InvalidInputException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CapacitySortBy {
  NAME("name"),
  TECH_COUNT("techCount");

  private final String displayName;

  public static CapacitySortBy getSortBy(String input) {
    return Arrays
        .stream(values())
        .filter(e -> e.name().equalsIgnoreCase(input) || e.displayName.equalsIgnoreCase(input))
        .findFirst()
        .orElseThrow(() -> new InvalidInputException(ErrorCode.INVALID_SORT_BY));
  }
}
