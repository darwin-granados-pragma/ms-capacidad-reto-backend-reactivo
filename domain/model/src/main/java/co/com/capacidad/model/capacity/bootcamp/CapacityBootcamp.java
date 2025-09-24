package co.com.capacidad.model.capacity.bootcamp;

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
public class CapacityBootcamp {

  private String id;
  private String idBootcamp;
  private String idCapacity;
  private boolean isNew;
}
