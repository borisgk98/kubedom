package space.borisgk98.kubedom.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import space.borisgk98.kubedom.api.model.entity.ProviderNode;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProviderNodeRepo extends JpaRepository<ProviderNode, Long> {
    Optional<ProviderNode> findByOwnerIdAndNodeUuid(Long ownerId, UUID nodeUuid);
    Optional<ProviderNode> findByNodeUuid(UUID nodeUuid);
    List<ProviderNode> findAllByOwnerId(Long ownerId);
}
