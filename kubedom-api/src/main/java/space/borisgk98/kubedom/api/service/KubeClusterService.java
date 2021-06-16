package space.borisgk98.kubedom.api.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import space.borisgk98.kubedom.api.model.dto.rest.ClusterCreationRequest;
import space.borisgk98.kubedom.api.model.entity.CustomerNode;
import space.borisgk98.kubedom.api.model.entity.KubeCluster;
import space.borisgk98.kubedom.api.model.entity.ProviderNode;
import space.borisgk98.kubedom.api.model.enums.CustomerNodeType;
import space.borisgk98.kubedom.api.model.enums.KubeClusterStatus;
import space.borisgk98.kubedom.api.model.enums.ProviderNodeState;
import space.borisgk98.kubedom.api.model.enums.ProviderNodeType;
import space.borisgk98.kubedom.api.repo.KubeClusterRepo;
import space.borisgk98.kubedom.api.security.SecurityService;
import space.borisgk98.kubedom.api.ws.WebSocketSender;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class KubeClusterService {

    private final KubeClusterRepo kubeClusterRepo;
    private final CustomerNodeService customerNodeService;
    private final WebSocketSender webSocketSender;
    private final AppUserService appUserService;
    private final SecurityService securityService;
    private final ProviderNodeService providerNodeService;

    // TODO разобраться с транзационностью и ассинхронностью
    @Transactional
    @SneakyThrows
    public KubeCluster create(ClusterCreationRequest clusterCreationRequest) {
        // TODO мультимастер ноды и валидация, обработка ошибок
        if (clusterCreationRequest.getMasterCount() != 1) {
            throw new Exception();
        }
        var user = securityService.getCurrAppUser();
        var cluster = new KubeCluster()
                .setStatus(KubeClusterStatus.PENDING)
                .setOwner(user);
        cluster = kubeClusterRepo.save(cluster);
        var providerNodes = providerNodeService.getAll().stream()
                .filter(node -> Objects.equals(node.getProviderNodeState(), ProviderNodeState.ACTIVE))
                .collect(Collectors.toList());
        var providerNodesForMaster = providerNodes.stream()
                .filter(providerNode -> Objects.equals(providerNode.getType(), ProviderNodeType.PRIMARY))
                .limit(clusterCreationRequest.getMasterCount())
                .collect(Collectors.toList());
        if (providerNodesForMaster.size() != clusterCreationRequest.getMasterCount()) {
            throw new Exception();
        }
        var providerNodesForWorker = providerNodes.stream()
                .filter(providerNode -> !providerNodesForMaster.contains(providerNode))
                // в приоритете - SECONDARY
                .sorted(Comparator.comparing(ProviderNode::getType).reversed())
                .limit(clusterCreationRequest.getWorkerCount())
                .collect(Collectors.toList());
        if (providerNodesForWorker.size() != clusterCreationRequest.getWorkerCount()) {
            throw new Exception();
        }
        cluster.setNodes(new ArrayList<>());
        List<CustomerNode> masters = new ArrayList<>();
        for (var providerNode : providerNodesForMaster) {
            var masterNode = customerNodeService.createPending(cluster, CustomerNodeType.MASTER);
            masters.add(masterNode);
            cluster.getNodes().add(masterNode);
            customerNodeService.deploy(masterNode, providerNode);
        }
        for (var providerNode : providerNodesForWorker) {
            var workerNode = customerNodeService.createPending(cluster, CustomerNodeType.WORKER);
            cluster.getNodes().add(workerNode);
            customerNodeService.deploy(workerNode, providerNode);
        }
        return kubeClusterRepo.save(cluster);
    }

    public void updateClusterStatus(Long kubeClusterId) {
        var kubeCluster = kubeClusterRepo.getById(kubeClusterId);
        if (kubeCluster.getNodes().stream().allMatch(CustomerNode::isReady)) {
            kubeCluster.setStatus(KubeClusterStatus.READY);
            kubeClusterRepo.save(kubeCluster);
        }
    }

    @Transactional
    public void delete(Long clusterId) {
        KubeCluster kubeCluster = kubeClusterRepo.getById(clusterId);
        for (var node : kubeCluster.getNodes()) {
            customerNodeService.delete(node.getId());
        }
        kubeClusterRepo.delete(kubeCluster);
    }

    public KubeCluster read(Long clusterId) {
        return kubeClusterRepo.getById(clusterId);
    }

    public List<KubeCluster> findByOwnerId(Long userId) {
        return kubeClusterRepo.findAllByOwnerId(userId);
    }
}
