package space.borisgk98.kubedom.api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import space.borisgk98.kubedom.api.model.enums.ProviderNodeState;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
            name="owner_id"
//            ,
//            nullable=false
    )
    private AppUser owner;

    @Column(name = "owner_id", insertable = false, updatable = false)
    private Long ownerId;

    @Column(name = "node_uuid")
    private UUID nodeUuid;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private ProviderNodeState providerNodeState;

    @OneToOne
    @JoinColumn(
            name="ws_session_id"
//            ,
//            nullable=false
    )
    private CurrWebSocketSession webSocketSession;

    @Column(name = "ws_session_id", insertable = false, updatable = false)
    private String webSocketSessionId;

    @Column(name = "external_ip")
    private String externalIp;
}
