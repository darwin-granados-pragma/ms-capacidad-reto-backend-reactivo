package co.com.capacidad.model.input;

import co.com.capacidad.model.capacity.CapacityResponse;
import co.com.capacidad.model.capacity.CapacitySortBy;
import co.com.capacidad.model.page.CapacityPageCommand;
import co.com.capacidad.model.page.PageResponse;
import reactor.core.publisher.Mono;

public interface CapacityRetrieveStrategy {

  CapacitySortBy getType();

  Mono<PageResponse<CapacityResponse>> getCapacityResponse(CapacityPageCommand command);
}
