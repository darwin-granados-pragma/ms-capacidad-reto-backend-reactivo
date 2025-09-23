package co.com.capacidad.model.page;

import co.com.capacidad.model.error.ErrorCode;
import co.com.capacidad.model.exception.InvalidInputException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortDirection {
  ASC("asc"),
  DESC("desc");

  private final String displayName;

  public static SortDirection getSortBy(String input) {
    return Arrays
        .stream(values())
        .filter(e -> e
            .name()
            .equalsIgnoreCase(input) || e.displayName.equalsIgnoreCase(input))
        .findFirst()
        .orElseThrow(() -> new InvalidInputException(ErrorCode.INVALID_SORT_BY));
  }
}
