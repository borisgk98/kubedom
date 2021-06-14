package space.borisgk98.kubedom.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import space.borisgk98.kubedom.api.exception.ModelNotFound;
import space.borisgk98.kubedom.api.mapping.ProviderNodeSearchMapper;
import space.borisgk98.kubedom.api.model.dto.rest.CustomerNodeCreationRequest;
import space.borisgk98.kubedom.api.model.dto.ws.WSCustomerNodeConfigDto;
import space.borisgk98.kubedom.api.model.dto.ws.WSCustomerNodeCreationDto;
import space.borisgk98.kubedom.api.model.dto.ws.WSCustomerNodeRemovingDto;
import space.borisgk98.kubedom.api.model.entity.AppUser;
import space.borisgk98.kubedom.api.model.entity.CurrWebSocketSession;
import space.borisgk98.kubedom.api.model.entity.CustomerNode;
import space.borisgk98.kubedom.api.model.entity.ProviderNode;
import space.borisgk98.kubedom.api.model.enums.CustomerNodeState;
import space.borisgk98.kubedom.api.repo.CustomerNodeRepo;
import space.borisgk98.kubedom.api.security.SecurityService;
import space.borisgk98.kubedom.api.ws.WebSocketSender;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
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
    public CustomerNode create(CustomerNodeCreationRequest dto) {
        ProviderNode providerNode = providerNodeService.search(providerNodeSearchMapper.unmap(dto))
                .stream().findFirst()
                .orElseThrow(ModelNotFound::new);
        if (providerNode.getWebSocketSession() == null) {
            // TODO
            throw new Exception();
        }
        AppUser owner = securityService.getCurrAppUser();
        CustomerNode customerNode = new CustomerNode()
                .setMachineName(generateMachineName())
                .setProviderNode(providerNode)
                .setOwner(owner);
        customerNode = customerNodeRepo.save(customerNode);
        var customerNodeCreationDto = new WSCustomerNodeCreationDto()
                .setOvaLocation(ovaLocation)
                .setCustomerNodeConfig(objectMapper.writeValueAsString(new WSCustomerNodeConfigDto()
                        .setCustomerNodeId(customerNode.getId())))
                .setMachineName(customerNode.getMachineName());
        webSocketSender.send(providerNode.getWebSocketSessionId(), customerNodeCreationDto);
        // TODO получить положительный ответ от provider-node-manager
        return customerNode;
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
