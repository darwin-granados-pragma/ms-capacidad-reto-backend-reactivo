package co.com.capacidad.api.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import co.com.capacidad.api.error.ErrorResponse;
import co.com.capacidad.api.error.GlobalErrorWebFilter;
import co.com.capacidad.api.handler.CapacityHandler;
import co.com.capacidad.api.model.request.CapacityCreateRequest;
import co.com.capacidad.api.model.response.CapacityResponse;
import co.com.capacidad.model.page.PageResponse;
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
public class CapacityRouterRest {

  private static final String PATH = "/api/v1/capacity";
  private static final String VALIDATE_CAPACITIES = PATH + "/validate";
  private static final String GET_CAPACITIES_BOOTCAMP = "/api/v1/bootcamp/{idBootcamp}/capacities";

  private final CapacityHandler capacityHandler;
  private final GlobalErrorWebFilter globalErrorWebFilter;

  @Bean
  @RouterOperations({@RouterOperation(method = RequestMethod.POST,
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
  ), @RouterOperation(method = RequestMethod.GET,
      path = PATH,
      beanClass = CapacityHandler.class,
      beanMethod = "findAll",
      operation = @Operation(operationId = "findAll",
          summary = "Obtener capacidades y tecnologías asociadas",
          description = "Recibe datos para parametrizar la búsqueda",
          parameters = {@Parameter(name = "sortBy",
              in = ParameterIn.QUERY,
              description = "Propiedad a ordenar",
              schema = @Schema(type = "String")
          ), @Parameter(name = "sortDirection",
              in = ParameterIn.QUERY,
              description = "Dirección de la lista (asc, desc)",
              schema = @Schema(type = "String")
          ), @Parameter(name = "page",
              in = ParameterIn.QUERY,
              description = "Número de pagina a recuperar",
              schema = @Schema(type = "Integer")
          ), @Parameter(name = "size",
              in = ParameterIn.QUERY,
              description = "Cantidad de registros por pagina",
              schema = @Schema(type = "Integer")
          )},
          responses = {@ApiResponse(responseCode = "200",
              description = "Lista de Capacidades recuperada",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = PageResponse.class)
              )
          )}
      )
  ), @RouterOperation(method = RequestMethod.POST,
      path = VALIDATE_CAPACITIES,
      beanClass = CapacityHandler.class,
      beanMethod = "validateCapacities",
      operation = @Operation(operationId = "validateCapacities",
          summary = "Validar capacidades",
          description = "Recibe lista de identificadores de las capacidades",
          requestBody = @RequestBody(required = true,
              content = @Content(mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(type = "String", example = "[id1, id2, id3]"
                  )
                  )
              )
          ),
          responses = {@ApiResponse(responseCode = "204", description = "Capacidades verificadas"
          ), @ApiResponse(responseCode = "404",
              description = "Capacidad no encontrada",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )
          )}
      )
  ), @RouterOperation(method = RequestMethod.GET,
      path = GET_CAPACITIES_BOOTCAMP,
      beanClass = CapacityHandler.class,
      beanMethod = "getCapacitiesByIdBootcamp",
      operation = @Operation(operationId = "getCapacitiesByIdBootcamp",
          summary = "Obtener capacidades por bootcamp",
          description = "Recibe el identificador del bootcamp y devuelve la lista de capacidades asociadas a ese bootcamp",
          parameters = {@Parameter(name = "idBootcamp",
              in = ParameterIn.PATH,
              required = true,
              description = "Identificador único del bootcamp",
              schema = @Schema(type = "string",
                  format = "uuid",
                  example = "a1b2c3d4-e5f6-7890-1234-567890abcdef"
              )
          )},
          responses = {@ApiResponse(responseCode = "200", description = "Capacidades recuperadas"
          ), @ApiResponse(responseCode = "404",
              description = "Bootcamp no encontrado",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )
          )}
      )
  )}
  )
  public RouterFunction<ServerResponse> routerFunction() {
    return route(POST(PATH), capacityHandler::createCapacity)
        .andRoute(GET(PATH), capacityHandler::findAll)
        .andRoute(POST(VALIDATE_CAPACITIES), capacityHandler::validateCapacities)
        .andRoute(GET(GET_CAPACITIES_BOOTCAMP), capacityHandler::getCapacitiesByIdBootcamp)
        .filter(globalErrorWebFilter);
  }
}
