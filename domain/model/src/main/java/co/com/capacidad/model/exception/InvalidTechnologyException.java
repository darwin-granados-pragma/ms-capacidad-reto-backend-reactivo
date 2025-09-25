package co.com.capacidad.model.exception;

import co.com.capacidad.model.error.ErrorCode;

public class InvalidTechnologyException extends ApplicationException {

  public InvalidTechnologyException(ErrorCode errorCode, String value) {
    super(errorCode, value);
  }
}
