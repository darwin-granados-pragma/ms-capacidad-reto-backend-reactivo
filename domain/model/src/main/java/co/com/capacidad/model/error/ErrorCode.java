package co.com.capacidad.model.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  CAPACITY_NAME_ALREADY_EXISTS("CAPACITY-NAME-ALREADY-EXISTS",
      ExceptionCode.CONSTRAINT_VIOLATION,
      "El nombre de la capacidad ya está registrado."
  ),
  CONSTRAINT_VIOLATION("CONSTRAINT-VIOLATION",
      ExceptionCode.CONSTRAINT_VIOLATION,
      "Violación de restricción de datos."
  ),
  CAPACITY_TECHNOLOGIES_SIZE("CAPACITY-TECHNOLOGIES-SIZE",
      ExceptionCode.INVALID_INPUT,
      "Una capacidad debe tener entre 3 y 20 tecnologías"
  ),
  CANNOT_TECHNOLOGIES_TO_CAPACITY("CANNOT-TECHNOLOGIES-CAPACITY",
      ExceptionCode.INVALID_INPUT,
      "Error al asignar tecnologías: "
  ),
  INVALID_TECHNOLOGIES("INVALID-TECHNOLOGIES",
      ExceptionCode.NOT_FOUND,
      "Error! "
  ),
  ;

  private final String fullErrorCode;
  private final ExceptionCode exceptionCode;
  private final String message;
}
