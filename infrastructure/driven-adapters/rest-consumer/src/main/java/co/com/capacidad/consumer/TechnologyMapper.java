package co.com.capacidad.consumer;

import co.com.capacidad.model.technology.Technology;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface TechnologyMapper {

  Technology toDomain(TechnologyResponse response);
}
