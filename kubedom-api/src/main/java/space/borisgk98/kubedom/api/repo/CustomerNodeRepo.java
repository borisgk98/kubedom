package space.borisgk98.kubedom.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import space.borisgk98.kubedom.api.model.entity.CustomerNode;

import java.util.List;
import java.util.Optional;

public interface CustomerNodeRepo extends JpaRepository<CustomerNode, Long> {
    Optional<CustomerNode> findByWebSocketSessionId(String sessionId);

    @Query("select node from CustomerNode node where node.customerNodeState = 'ACTIVE' " +
            "and node.ready = false " +
            "and node.type = 'MASTER'")
    List<CustomerNode> findWaitingMasters();

    @Query(value = "with clusters as (\n" +
            "    select distinct c.id as id from kube_cluster c join customer_node cn on c.id = cn.kube_cluster_id\n" +
            "    where cn.type = 'MASTER' and cn.ready = true\n" +
            ")\n" +
            "select n.* from customer_node n where n.type = 'WORKER' and n.ready = false and n.kube_cluster_id in (select id from clusters);", nativeQuery = true)
    List<CustomerNode> findWaitingWorkers();

    @Query("select node from CustomerNode node where node.ready = true and node.type = 'MASTER' and node.kubeClusterId = :clusterId")
    Optional<CustomerNode> findReadyMasterByClusterId(Long clusterId);
}
