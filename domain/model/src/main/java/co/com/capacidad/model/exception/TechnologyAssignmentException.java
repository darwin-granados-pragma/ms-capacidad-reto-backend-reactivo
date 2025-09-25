package co.com.capacidad.model.exception;

import co.com.capacidad.model.error.ErrorCode;

public class TechnologyAssignmentException extends ApplicationException {

  public TechnologyAssignmentException(ErrorCode errorCode, String value) {
    super(errorCode, value);
  }
}
