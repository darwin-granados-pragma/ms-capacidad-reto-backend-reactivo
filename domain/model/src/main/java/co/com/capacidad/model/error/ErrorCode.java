package co.com.capacidad.model.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  CAPACITY_NOT_FOUND("CAPACITY-NOT-FOUND",
      ExceptionCode.NOT_FOUND,
      "No se encontró la capacidad con id: "
  ),
  BOOTCAMP_NOT_FOUND("BOOTCAMP-NOT-FOUND",
      ExceptionCode.NOT_FOUND,
      "No se encontró el bootcamp con id: "
  ),
  CAPACITY_TECHNOLOGIES_SIZE("CAPACITY-TECHNOLOGIES-SIZE",
      ExceptionCode.INVALID_INPUT,
      "Una capacidad debe tener entre 3 y 20 tecnologías"
  ),
  CANNOT_TECHNOLOGIES_TO_CAPACITY("CANNOT-TECHNOLOGIES-CAPACITY",
      ExceptionCode.INVALID_INPUT,
      "Error al asignar tecnologías: "
  ),
  INVALID_TECHNOLOGIES("INVALID-TECHNOLOGIES", ExceptionCode.NOT_FOUND, "Error! "),
  INVALID_SORT_BY("INVALID-SORT-BY", ExceptionCode.INVALID_INPUT, "Parámetro sortBy no válido."),
  INVALID_PARAMETERS("INVALID-PARAMETERS", ExceptionCode.INVALID_INPUT, "Parámetros inválidos"),
  ;

  private final String fullErrorCode;
  private final ExceptionCode exceptionCode;
  private final String message;
}
