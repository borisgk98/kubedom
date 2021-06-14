package space.borisgk98.kubedom.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import space.borisgk98.kubedom.api.model.entity.KubeCluster;

public interface KubeClusterRepo extends JpaRepository<KubeCluster, Long> {
}
