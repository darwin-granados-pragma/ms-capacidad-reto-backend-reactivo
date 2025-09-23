package co.com.capacidad.model.page;

import co.com.capacidad.model.capacity.CapacitySortBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CapacityPageCommand {

  private int page;
  private int size;
  private CapacitySortBy sortBy;
  private SortDirection sortDirection;
}
