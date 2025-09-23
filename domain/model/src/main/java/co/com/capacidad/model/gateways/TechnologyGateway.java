package co.com.capacidad.model.gateways;

import java.util.Set;
import reactor.core.publisher.Mono;

public interface TechnologyGateway {

  Mono<Void> assignTechnologyToCapacity(String idCapacity, Set<String> technologies);

  Mono<Void> validateTechnologies(Set<String> technologies);
}
