package space.borisgk98.kubedom.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import space.borisgk98.kubedom.api.exception.ModelNotFound;
import space.borisgk98.kubedom.api.mapping.ProviderNodeSearchMapper;
import space.borisgk98.kubedom.api.model.dto.ws.WSCustomerNodeConfigDto;
import space.borisgk98.kubedom.api.model.dto.ws.WSCustomerNodeCreationDto;
import space.borisgk98.kubedom.api.model.dto.ws.WSCustomerNodeRemovingDto;
import space.borisgk98.kubedom.api.model.dto.ws.WSK3sMasterCreationDto;
import space.borisgk98.kubedom.api.model.dto.ws.WSK3sWorkerCreationDto;
import space.borisgk98.kubedom.api.model.entity.CurrWebSocketSession;
import space.borisgk98.kubedom.api.model.entity.CustomerNode;
import space.borisgk98.kubedom.api.model.entity.KubeCluster;
import space.borisgk98.kubedom.api.model.entity.ProviderNode;
import space.borisgk98.kubedom.api.model.enums.CustomerNodeState;
import space.borisgk98.kubedom.api.model.enums.CustomerNodeType;
import space.borisgk98.kubedom.api.repo.CustomerNodeRepo;
import space.borisgk98.kubedom.api.security.SecurityService;
import space.borisgk98.kubedom.api.ws.WebSocketSender;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.util.Objects;
import java.util.UUID;

import static java.lang.Thread.sleep;

@Service
@Slf4j
public class CustomerNodeService extends AbstractCrudService<CustomerNode, Long> {
    @Autowired
    private ProviderNodeService providerNodeService;
    @Autowired
    private WebSocketSender webSocketSender;
    @Autowired
    private CustomerNodeRepo customerNodeRepo;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private ProviderNodeSearchMapper providerNodeSearchMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${app.ova-location}")
    private String ovaLocation;

    public CustomerNodeService(JpaRepository<CustomerNode, Long> repository, EntityManager em, CriteriaBuilder cb) {
        super(repository, em, cb);
    }

    // TODO exception processing
    @SneakyThrows
    @Transactional
    public CustomerNode createPending(KubeCluster kubeCluster, CustomerNodeType customerNodeType) {
        CustomerNode customerNode = new CustomerNode()
                .setMachineName(generateMachineName())
                .setOwner(kubeCluster.getOwner())
                .setType(customerNodeType)
                .setKubeCluster(kubeCluster)
                .setCustomerNodeState(CustomerNodeState.PENDING);
        return customerNodeRepo.save(customerNode);
    }

    @SneakyThrows
    public void deploy(CustomerNode customerNode, ProviderNode providerNode) {
        customerNode.setProviderNode(providerNode);
        customerNode.setProviderNodeId(providerNode.getId());
        customerNode.setCustomerNodeState(CustomerNodeState.PENDING);
        update(customerNode);
        var customerNodeCreationDto = new WSCustomerNodeCreationDto()
                .setOvaLocation(ovaLocation)
                .setCustomerNodeConfig(objectMapper.writeValueAsString(new WSCustomerNodeConfigDto()
                        .setCustomerNodeId(customerNode.getId())))
                .setMachineName(customerNode.getMachineName());
        webSocketSender.send(providerNode.getWebSocketSessionId(), customerNodeCreationDto);
    }

    @SneakyThrows
    @Async
    public void deployK3sMaster(Long customerNodeId) {
        while (!Objects.equals(read(customerNodeId).getCustomerNodeState(), CustomerNodeState.ACTIVE)) {
            log.info("Waiting customer node {} ready", customerNodeId);
            sleep(15000);
        }
        var stored = read(customerNodeId);
        webSocketSender.send(
                stored.getWebSocketSessionId(),
                new WSK3sMasterCreationDto()
                        .setExternalIp(stored.getProviderNode().getExternalIp())
                        .setNodeName(String.format("master-%d", customerNodeId))
        );
    }

    @SneakyThrows
    @Async
    public void deployK3sWorker(Long workerNodeId, Long masterNodeId) {
        while (!Objects.equals(read(workerNodeId).getCustomerNodeState(), CustomerNodeState.ACTIVE)) {
            log.info("Waiting customer node {} ready", workerNodeId);
            sleep(15000);
        }
        while (!checkMaster(masterNodeId)) {
            log.info("Waiting master customer node {} ready", masterNodeId);
            sleep(15000);
        }
        var storedWorker = read(workerNodeId);
        var storedMaster = read(masterNodeId);
        webSocketSender.send(
                storedWorker.getWebSocketSessionId(),
                new WSK3sWorkerCreationDto()
                        .setMasterExternalIp(storedMaster.getProviderNode().getExternalIp())
                        .setMasterToken(storedMaster.getMasterToken())
                        .setNodeName(String.format("worker-%d", workerNodeId))
        );
    }

    private boolean checkMaster(Long masterNodeId) {
        CustomerNode master = read(masterNodeId);
        return Objects.equals(master.getCustomerNodeState(), CustomerNodeState.ACTIVE) && master.isReady();
    }

    public void updateSession(Long nodeId, CurrWebSocketSession webSocketSession) {
        repository.findById(nodeId)
                .ifPresent(node -> repository.save(node
                        .setWebSocketSession(webSocketSession)
                        .setCustomerNodeState(CustomerNodeState.ACTIVE)));
    }

    public void clearSession(Long nodeId) {
        repository.findById(nodeId)
                .ifPresent(node -> repository.save(node
                        .setWebSocketSession(null)
                        .setCustomerNodeState(CustomerNodeState.DETACHED)));
    }

    // TODO нормальные названия
    private String generateMachineName() {
        return "VM:" + UUID.randomUUID().toString();
    }

    /**
     * Регистрация новой ноды либо проверка того, что нода уже существует
     */
    public boolean register(Long nodeId) {
        return repository.findById(nodeId).isPresent();
    }

    @Override
    @Transactional
    public void delete(Long id) throws ModelNotFound {
        CustomerNode customerNode = read(id);
        ProviderNode providerNode = customerNode.getProviderNode();
        webSocketSender.send(providerNode.getWebSocketSessionId(), new WSCustomerNodeRemovingDto()
                .setMachineName(customerNode.getMachineName()));
        repository.delete(customerNode);
    }

    public CustomerNode getBySessionId(String sessionId) {
        return ((CustomerNodeRepo) repository).findByWebSocketSessionId(sessionId).orElseThrow(ModelNotFound::new);
    }
}
