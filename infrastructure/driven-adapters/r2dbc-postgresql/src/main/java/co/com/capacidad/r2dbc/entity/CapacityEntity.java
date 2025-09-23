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
@Table(name = "capacidad")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CapacityEntity implements Persistable<String> {

  @Id
  @Column("id_capacidad")
  private String id;

  @Column("nombre")
  private String name;

  @Column("descripcion")
  private String description;

  @Transient
  private boolean isNew;

  @Override
  public boolean isNew() {
    return this.isNew || this.id == null;
  }
}
