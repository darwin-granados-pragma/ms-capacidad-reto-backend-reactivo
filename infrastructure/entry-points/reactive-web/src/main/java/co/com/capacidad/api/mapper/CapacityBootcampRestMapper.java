package co.com.capacidad.api.mapper;

import co.com.capacidad.model.capacity.bootcamp.CapacityBootcampCreate;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CapacityBootcampRestMapper {

  CapacityBootcampCreate toCapacityBootcampCreate(String idBootcamp, Set<String> capacities);
}
