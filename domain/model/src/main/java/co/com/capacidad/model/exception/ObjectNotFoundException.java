package co.com.capacidad.model.exception;

import co.com.capacidad.model.error.ErrorCode;

public class ObjectNotFoundException extends ApplicationException {
  public ObjectNotFoundException(ErrorCode errorCode, String value) {
    super(errorCode, value);
  }

}
