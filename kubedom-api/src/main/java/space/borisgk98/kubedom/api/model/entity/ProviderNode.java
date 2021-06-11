package space.borisgk98.kubedom.api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "provider_node")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderNode implements IEntity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name="provider_id"
//            ,
//            nullable=false
    )
    private Provider provider;

    @Column(name = "provider_id", insertable = false, updatable = false)
    private Long providerId;

    @Column(name = "node_uuid")
    private UUID nodeUuid;

    @OneToOne
    @JoinColumn(
            name="ws_session_id"
//            ,
//            nullable=false
    )
    private CurrWebSocketSession webSocketSession;

    @Column(name = "ws_session_id", insertable = false, updatable = false)
    private String webSocketSessionId;
}
