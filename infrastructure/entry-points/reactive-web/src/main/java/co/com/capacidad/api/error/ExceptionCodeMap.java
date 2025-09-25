package co.com.capacidad.api.error;

import co.com.capacidad.model.error.ExceptionCode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ExceptionCodeMap {

  public HttpStatus getHttpStatusFromExceptionCode(ExceptionCode exceptionCode) {
    return switch (exceptionCode) {
      case INVALID_INPUT -> HttpStatus.BAD_REQUEST;
      case NOT_FOUND -> HttpStatus.NOT_FOUND;
      case CONSTRAINT_VIOLATION -> HttpStatus.CONFLICT;
      case UNEXPECTED_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
    };
  }
}
