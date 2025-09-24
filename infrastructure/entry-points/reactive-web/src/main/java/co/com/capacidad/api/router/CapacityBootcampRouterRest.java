package co.com.capacidad.api.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import co.com.capacidad.api.error.GlobalErrorWebFilter;
import co.com.capacidad.api.handler.CapacityBootcampHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
public class CapacityBootcampRouterRest {

  private static final String PATH = "/api/v1/bootcamp/{id}/capacities";

  private final CapacityBootcampHandler capacityBootcampHandler;
  private final GlobalErrorWebFilter globalErrorWebFilter;

  @Bean
  @RouterOperation(method = RequestMethod.POST,
      path = PATH,
      beanClass = CapacityBootcampHandler.class,
      beanMethod = "createCapacityBootcamp",
      operation = @Operation(operationId = "createCapacityBootcamp",
          summary = "Asignar capacidades a un bootcamp",
          description = "Recibe datos de las capacidades y del bootcamp.",
          requestBody = @RequestBody(required = true,
              content = @Content(mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(type = "String", example = "[id1, id2, id3]"
                  )
                  )
              )
          ),
          responses = {@ApiResponse(responseCode = "204",
              description = "Capacidades asignadas al bootcamp correctamente"
          )}
      )
  )
  public RouterFunction<ServerResponse> capacityBootcampRouterFunction() {
    return route(POST(PATH), capacityBootcampHandler::createCapacityBootcamp).filter(
        globalErrorWebFilter);
  }
}
