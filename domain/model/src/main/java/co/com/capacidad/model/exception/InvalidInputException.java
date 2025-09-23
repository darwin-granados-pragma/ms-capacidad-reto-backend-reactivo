package co.com.capacidad.model.exception;

import co.com.capacidad.model.error.ErrorCode;

public class InvalidInputException extends ApplicationException {

  public InvalidInputException(ErrorCode errorCode) {
    super(errorCode);
  }
}
