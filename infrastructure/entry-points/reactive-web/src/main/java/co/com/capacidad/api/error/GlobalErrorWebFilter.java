package co.com.capacidad.api.error;

import co.com.capacidad.model.exception.ApplicationException;
import co.com.capacidad.model.exception.ValidationException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class GlobalErrorWebFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

  private final ExceptionCodeMap exceptionCodeMap;

  @Override
  public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
    return next
        .handle(request)
        .onErrorResume(ApplicationException.class, ex -> {
              HttpStatus status = exceptionCodeMap.getHttpStatusFromExceptionCode(ex.getExceptionCode());
              ErrorResponse error = buildErrorResponse(request, status, ex.getMessage(), null);
              log.debug("ApplicationException caught: {} - {}", ex.getFullErrorCode(), ex.getMessage());
              return buildServerResponse(error, status);
            }
        )
        .onErrorResume(ValidationException.class, ex -> {
              ErrorResponse error = buildErrorResponse(request,
                  HttpStatus.BAD_REQUEST,
                  "Parámetros inválidos o faltantes en la solicitud",
                  ex.getDetails()
              );
              return buildServerResponse(error, HttpStatus.BAD_REQUEST);
            }
        )
        .onErrorResume(Exception.class, ex -> {
              ErrorResponse error = buildErrorResponse(request,
                  HttpStatus.INTERNAL_SERVER_ERROR,
                  "An unexpected error occurred",
                  null
              );
              log.error("Unexpected error", ex);
              return buildServerResponse(error, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        );
  }


  private ErrorResponse buildErrorResponse(ServerRequest request, HttpStatus status, String message,
      List<String> details) {
    return ErrorResponse
        .builder()
        .timestamp(ZonedDateTime
            .now(ZoneOffset.UTC)
            .toString())
        .path(request.path())
        .status(status.value())
        .error(message)
        .requestId(request
            .exchange()
            .getRequest()
            .getId())
        .details(details)
        .build();
  }

  private Mono<ServerResponse> buildServerResponse(ErrorResponse error, HttpStatus status) {
    return ServerResponse
        .status(status)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(error);
  }
}
