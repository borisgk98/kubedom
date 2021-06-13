package space.borisgk98.kubedom.api.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import space.borisgk98.kubedom.api.model.enums.CustomerNodeState;
import space.borisgk98.kubedom.api.model.enums.CustomerNodeType;

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

@Entity
@Table(name = "customer_node")
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class CustomerNode implements IEntity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private CustomerNodeType type;

    @ManyToOne
    @JoinColumn(
            name="owner_id"
//            ,
//            nullable=false
    )
    private AppUser owner;

    @Column(name = "owner_id", insertable = false, updatable = false)
    private Long ownerId;

    @ManyToOne
    @JoinColumn(
            name="provider_node_id"
//            ,
//            nullable=false
    )
    private ProviderNode providerNode;

    @Column(name = "provider_node_id", insertable = false, updatable = false)
    private Long providerNodeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "node_state")
    private CustomerNodeState customerNodeState;

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
