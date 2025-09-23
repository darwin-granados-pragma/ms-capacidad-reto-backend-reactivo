package co.com.capacidad.model.exception;

import co.com.capacidad.model.error.ErrorCode;

public class InvalidFormatParamException extends ApplicationException {

  public InvalidFormatParamException(ErrorCode errorCode) {
    super(errorCode);
  }
}
