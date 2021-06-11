package space.borisgk98.kubedom.api.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ws_session")
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class CurrWebSocketSession implements IEntity<String> {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;
}
