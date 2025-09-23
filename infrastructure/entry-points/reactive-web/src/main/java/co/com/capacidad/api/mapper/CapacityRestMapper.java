package co.com.capacidad.api.mapper;

import co.com.capacidad.api.model.request.CapacityCreateRequest;
import co.com.capacidad.api.model.response.CapacityResponse;
import co.com.capacidad.model.capacity.Capacity;
import co.com.capacidad.model.capacity.CapacityCreate;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CapacityRestMapper {

  CapacityCreate toCapacityCreate(CapacityCreateRequest request);

  CapacityResponse toCapacityResponse(Capacity capacity);
}
