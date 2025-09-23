package co.com.capacidad.api.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import co.com.capacidad.api.error.ErrorResponse;
import co.com.capacidad.api.error.GlobalErrorWebFilter;
import co.com.capacidad.api.handler.CapacityHandler;
import co.com.capacidad.api.model.request.CapacityCreateRequest;
import co.com.capacidad.api.model.response.CapacityResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class CapacityRouterRest {

  private static final String PATH = "/api/v1/capacity";

  private final CapacityHandler capacityHandler;
  private final GlobalErrorWebFilter globalErrorWebFilter;

  @Bean
  @RouterOperation(method = RequestMethod.POST,
      path = PATH,
      beanClass = CapacityHandler.class,
      beanMethod = "createCapacity",
      operation = @Operation(operationId = "createCapacity",
          summary = "Crear capacidad",
          description = "Recibe datos de la capacidad y devuelve el objeto creado",
          requestBody = @RequestBody(required = true,
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = CapacityCreateRequest.class)
              )
          ),
          responses = {@ApiResponse(responseCode = "200",
              description = "Capacidad creada correctamente",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = CapacityResponse.class)
              )
          ), @ApiResponse(responseCode = "400",
              description = "Parámetros inválidos o faltantes",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )
          ), @ApiResponse(responseCode = "409",
              description = "Capacidad con el nombre ingresado ya se encuentra registrada",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )
          )}
      )
  )
  public RouterFunction<ServerResponse> routerFunction() {
    return route(POST(PATH), capacityHandler::createCapacity).filter(globalErrorWebFilter);
  }
}
