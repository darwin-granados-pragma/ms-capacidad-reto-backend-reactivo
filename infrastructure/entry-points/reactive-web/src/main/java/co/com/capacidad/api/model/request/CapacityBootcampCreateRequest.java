package co.com.capacidad.api.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
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
public class CapacityBootcampCreateRequest {

  @NotBlank(message = "El identificador del bootcamp es obligatorio")
  @Size(max = 50, message = "El identificador del bootcamp debe tener menos de 50 caracteres")
  private String idBootcamp;

  @NotNull(message = "Las capacidades son obligatorias")
  private Set<String> idCapacities;
}
