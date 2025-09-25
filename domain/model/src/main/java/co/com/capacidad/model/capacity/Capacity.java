package co.com.capacidad.model.capacity;

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
public class Capacity {

  private String id;
  private String name;
  private String description;
  private boolean isNew;
}
