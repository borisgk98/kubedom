package space.borisgk98.kubedom.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import space.borisgk98.kubedom.api.model.entity.CustomerNode;

import java.util.Optional;

public interface CustomerNodeRepo extends JpaRepository<CustomerNode, Long> {
    Optional<CustomerNode> findByWebSocketSessionId(String sessionId);
}
