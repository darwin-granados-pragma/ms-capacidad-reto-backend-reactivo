package co.com.capacidad.model.exception;

import co.com.capacidad.model.error.ErrorCode;

public class BusinessException extends ApplicationException {

  public BusinessException(ErrorCode errorCode) {
    super(errorCode);
  }
}
