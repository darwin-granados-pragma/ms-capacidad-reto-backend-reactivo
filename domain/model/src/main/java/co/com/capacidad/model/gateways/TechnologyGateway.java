package co.com.capacidad.model.gateways;

import co.com.capacidad.model.technology.Technology;
import java.util.Set;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TechnologyGateway {

  Mono<Void> assignTechnologyToCapacity(String idCapacity, Set<String> technologies);

  Mono<Void> validateTechnologies(Set<String> technologies);

  Flux<Technology> getTechnologiesByCapacityId(String idCapacity);

  Mono<Void> deleteRelationsCapacityTechnologies(String idCapacity);

  Mono<Long> countTechnologiesByCapacityId(String idCapacity);
}
