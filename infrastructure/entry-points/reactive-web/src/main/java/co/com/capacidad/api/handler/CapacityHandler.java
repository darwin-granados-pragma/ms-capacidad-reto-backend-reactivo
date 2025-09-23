package co.com.capacidad.api.handler;

import co.com.capacidad.api.mapper.CapacityRestMapper;
import co.com.capacidad.api.model.request.CapacityCreateRequest;
import co.com.capacidad.model.capacity.CapacityCreate;
import co.com.capacidad.usecase.capacity.CapacityUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
