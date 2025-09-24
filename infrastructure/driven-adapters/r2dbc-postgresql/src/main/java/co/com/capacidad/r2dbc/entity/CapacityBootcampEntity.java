package co.com.capacidad.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table(name = "capacidad_bootcamp")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CapacityBootcampEntity implements Persistable<String> {

  @Id
  @Column("id_capacidad_bootcamp")
  private String id;

  @Column("id_bootcamp")
  private String idBootcamp;

  @Column("id_capacidad")
  private String idCapacity;

  @Transient
  private boolean isNew;

  @Override
  public boolean isNew() {
    return this.isNew || this.id == null;
  }
}
