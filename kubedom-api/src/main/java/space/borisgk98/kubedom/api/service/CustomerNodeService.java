package space.borisgk98.kubedom.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import space.borisgk98.kubedom.api.exception.ModelNotFound;
import space.borisgk98.kubedom.api.mapping.ProviderNodeSearchMapper;
import space.borisgk98.kubedom.api.model.dto.rest.CustomerNodeCreationRequest;
import space.borisgk98.kubedom.api.model.dto.ws.WSCustomerNodeCreationDto;
import space.borisgk98.kubedom.api.model.entity.AppUser;
import space.borisgk98.kubedom.api.model.entity.CustomerNode;
import space.borisgk98.kubedom.api.model.entity.ProviderNode;
import space.borisgk98.kubedom.api.repo.CustomerNodeRepo;
import space.borisgk98.kubedom.api.security.SecurityService;
import space.borisgk98.kubedom.api.ws.WebSocketSender;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;

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

    @Value("${app.ova-location}")
    private String ovaLocation;

    public CustomerNodeService(JpaRepository<CustomerNode, Long> repository, EntityManager em, CriteriaBuilder cb) {
        super(repository, em, cb);
    }

    public void create(CustomerNodeCreationRequest dto) {
        ProviderNode providerNode = providerNodeService.search(providerNodeSearchMapper.unmap(dto))
                .stream().findFirst()
                .orElseThrow(ModelNotFound::new);
        if (providerNode.getWebSocketSession() == null) {
            return;
        }
        AppUser owner = securityService.getCurrAppUser();
        CustomerNode customerNode = new CustomerNode()
                .setProviderNode(providerNode)
                .setOwner(owner);
        customerNodeRepo.save(customerNode);
        var customerNodeCreationDto = new WSCustomerNodeCreationDto();
        customerNodeCreationDto.setOvaLocation(ovaLocation);
        webSocketSender.send(providerNode.getWebSocketSessionId(), customerNodeCreationDto);
    }
}
