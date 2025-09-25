package co.com.capacidad.model.capacity;

import java.util.Set;

public record CapacityCreate(String name, String description, Set<String> idTechnologies) {

}
