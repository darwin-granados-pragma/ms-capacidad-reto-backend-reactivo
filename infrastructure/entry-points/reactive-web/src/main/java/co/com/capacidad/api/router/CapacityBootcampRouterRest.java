package co.com.capacidad.api.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import co.com.capacidad.api.error.GlobalErrorWebFilter;
import co.com.capacidad.api.handler.CapacityBootcampHandler;
import co.com.capacidad.api.model.response.CapacityTechnologyRestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class CapacityBootcampRouterRest {

  private static final String PATH_CREATE = "/api/v1/bootcamp/{id}/capacities";
  private static final String PATH_COUNT = "/api/v1/bootcamp/{id}/count";

  private final CapacityBootcampHandler capacityBootcampHandler;
  private final GlobalErrorWebFilter globalErrorWebFilter;

  @Bean
  @RouterOperations({@RouterOperation(method = RequestMethod.POST,
      path = PATH_CREATE,
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
  ), @RouterOperation(method = RequestMethod.GET,
      path = PATH_COUNT,
      beanClass = CapacityBootcampHandler.class,
      beanMethod = "getCountCapAndTechByIdBootcamp",
      operation = @Operation(operationId = "getCountCapAndTechByIdBootcamp",
          summary = "Obtiene el conteo de capacidades y tecnologías por bootcamp",
          description = "Obtiene el conteo de capacidades y tecnologías por bootcamp según su id.",
          parameters = {@Parameter(name = "id",
              in = ParameterIn.PATH,
              description = "Identificador del bootcamp",
              required = true,
              schema = @Schema(type = "String")
          )},
          responses = {@ApiResponse(responseCode = "200",
              description = "Conteo de capacidades y tecnologías por bootcamp.",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = CapacityTechnologyRestResponse.class)
              )
          )}
      )
  )}
  )
  public RouterFunction<ServerResponse> capacityBootcampRouterFunction() {
    return route(POST(PATH_CREATE), capacityBootcampHandler::createCapacityBootcamp)
        .andRoute(GET(PATH_COUNT), capacityBootcampHandler::getCountCapAndTechByIdBootcamp)
        .filter(globalErrorWebFilter);
  }
}
