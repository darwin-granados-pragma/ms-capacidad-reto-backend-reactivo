package co.com.capacidad.api.handler;

import co.com.capacidad.api.mapper.CapacityRestMapper;
import co.com.capacidad.api.model.request.CapacityCreateRequest;
import co.com.capacidad.model.capacity.CapacityCreate;
import co.com.capacidad.model.capacity.CapacitySortBy;
import co.com.capacidad.model.error.ErrorCode;
import co.com.capacidad.model.exception.InvalidFormatParamException;
import co.com.capacidad.model.page.CapacityPageCommand;
import co.com.capacidad.model.page.SortDirection;
import co.com.capacidad.usecase.capacity.CapacityUseCase;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class CapacityHandler {

  private final CapacityUseCase useCase;
  private final CapacityRestMapper mapper;
  private final RequestValidator requestValidator;

  public Mono<ServerResponse> createCapacity(ServerRequest serverRequest) {
    log.info("Received request to create a capacity at path={} method={}",
        serverRequest.path(),
        serverRequest.method()
    );
    return serverRequest
        .bodyToMono(CapacityCreateRequest.class)
        .flatMap(request -> requestValidator
            .validate(request)
            .then(Mono.defer(() -> {
              CapacityCreate capacityCreate = mapper.toCapacityCreate(request);
              return useCase
                  .createCapacity(capacityCreate)
                  .map(mapper::toCapacityResponse)
                  .flatMap(response -> ServerResponse
                      .status(HttpStatus.CREATED)
                      .contentType(MediaType.APPLICATION_JSON)
                      .bodyValue(response));
            })));
  }

  public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
    log.info("Request received for capacity list: path={}, method={}",
        serverRequest.path(),
        serverRequest.method()
    );
    return Mono
        .defer(() -> {
          try {
            String sortByParam = serverRequest
                .queryParam("sortBy")
                .orElse("name");
            String directionParam = serverRequest
                .queryParam("sortDirection")
                .orElse("ASC");
            int page = Integer.parseInt(serverRequest
                .queryParam("page")
                .orElse("0"));
            int size = Integer.parseInt(serverRequest
                .queryParam("size")
                .orElse("10"));

            CapacityPageCommand command = CapacityPageCommand
                .builder()
                .page(page)
                .size(size)
                .sortBy(CapacitySortBy.getSortBy(sortByParam))
                .sortDirection(SortDirection.getSortBy(directionParam))
                .build();

            return useCase.getCapacities(command);

          } catch (IllegalArgumentException ex) {
            log.error("Invalid parameter format", ex);
            return Mono.error(new InvalidFormatParamException(ErrorCode.INVALID_PARAMETERS));
          }
        })
        .flatMap(pageResponse -> ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(pageResponse));
  }

  public Mono<ServerResponse> validateCapacities(ServerRequest serverRequest) {
    log.info("Received request to validate capacities at path={} method={}",
        serverRequest.path(),
        serverRequest.method()
    );
    return serverRequest
        .bodyToMono(new ParameterizedTypeReference<Set<String>>() {
        })
        .flatMap(useCase::validateCapacities)
        .then(ServerResponse
            .noContent()
            .build());
  }
}
