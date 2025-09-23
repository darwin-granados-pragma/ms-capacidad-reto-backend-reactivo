package co.com.capacidad.consumer;

import co.com.capacidad.model.error.ErrorCode;
import co.com.capacidad.model.exception.InvalidTechnologyException;
import co.com.capacidad.model.exception.TechnologyAssignmentException;
import co.com.capacidad.model.gateways.TechnologyGateway;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer implements TechnologyGateway {

  private final WebClient client;

  @Override
  public Mono<Void> assignTechnologyToCapacity(String idCapacity, Set<String> technologies) {
    return client
        .post()
        .uri("/api/v1/capacities/{id}/technologies", idCapacity)
        .bodyValue(technologies)
        .retrieve()
        .onStatus(HttpStatusCode::isError,
            clientResponse -> clientResponse
                .bodyToMono(ErrorResponse.class)
                .flatMap(errorBody -> Mono.error(new TechnologyAssignmentException(ErrorCode.CANNOT_TECHNOLOGIES_TO_CAPACITY,
                    errorBody.error()
                )))
        )
        .bodyToMono(Void.class);
  }

  @Override
  public Mono<Void> validateTechnologies(Set<String> technologies) {
    return client
        .post()
        .uri("/api/v1/tecnologia/validate")
        .bodyValue(technologies)
        .retrieve()
        .onStatus(HttpStatusCode::isError,
            clientResponse -> clientResponse
                .bodyToMono(ErrorResponse.class)
                .flatMap(errorBody -> Mono.error(new InvalidTechnologyException(ErrorCode.INVALID_TECHNOLOGIES,
                    errorBody.error()
                )))
        )
        .bodyToMono(Void.class);
  }
}
