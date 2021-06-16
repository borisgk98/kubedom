package space.borisgk98.kubedom.api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import space.borisgk98.kubedom.api.model.enums.KubeClusterStatus;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "kube_cluster")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KubeCluster implements IEntity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "kubeCluster", fetch = FetchType.EAGER)
    private List<CustomerNode> nodes;

    @ManyToOne
    @JoinColumn(
            name="owner_id"
//            ,
//            nullable=false
    )
    private AppUser owner;

    @Column(name = "owner_id", insertable = false, updatable = false)
    private Long ownerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private KubeClusterStatus status;
}
