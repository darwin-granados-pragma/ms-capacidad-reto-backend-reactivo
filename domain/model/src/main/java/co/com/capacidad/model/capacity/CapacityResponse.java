package co.com.capacidad.model.capacity;

import co.com.capacidad.model.technology.Technology;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CapacityResponse {

  private String id;
  private String name;
  private String description;
  private List<Technology> technologies;
}
