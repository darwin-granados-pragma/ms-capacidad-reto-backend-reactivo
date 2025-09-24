package co.com.capacidad.api.handler;

import co.com.capacidad.api.mapper.CapacityBootcampRestMapper;
import co.com.capacidad.usecase.capacity.bootcamp.CapacityBootcampUseCase;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class CapacityBootcampHandler {

  private final CapacityBootcampUseCase useCase;
  private final CapacityBootcampRestMapper mapper;

  public Mono<ServerResponse> createCapacityBootcamp(ServerRequest serverRequest) {
    log.info("Received request to save capacities to a bootcamp at path={} method={}",
        serverRequest.path(),
        serverRequest.method()
    );
    return Mono.defer(() -> {
      String idBootcamp = serverRequest.pathVariable("id");
      return serverRequest
          .bodyToMono(new ParameterizedTypeReference<Set<String>>() {
          })
          .flatMap(idCapacities -> useCase.assignCapacitiesToBootcamp(mapper
              .toCapacityBootcampCreate(idBootcamp, idCapacities
          )))
          .then(ServerResponse
              .noContent()
              .build());
    });
  }

}
