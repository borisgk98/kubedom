package space.borisgk98.kubedom.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import space.borisgk98.kubedom.api.model.entity.KubeCluster;

import java.util.List;

public interface KubeClusterRepo extends JpaRepository<KubeCluster, Long> {
    List<KubeCluster> findAllByOwnerId(Long ownerId);
}
