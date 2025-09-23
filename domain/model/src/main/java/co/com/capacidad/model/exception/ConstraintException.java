package co.com.capacidad.model.exception;

import co.com.capacidad.model.error.ErrorCode;

public class ConstraintException extends ApplicationException {

  public ConstraintException(ErrorCode errorCode) {
    super(errorCode);
  }
}
