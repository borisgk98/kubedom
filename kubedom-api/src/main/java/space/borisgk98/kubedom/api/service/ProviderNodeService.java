package space.borisgk98.kubedom.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import space.borisgk98.kubedom.api.model.dto.rest.ProviderNodeSearchRequest;
import space.borisgk98.kubedom.api.model.entity.CurrWebSocketSession;
import space.borisgk98.kubedom.api.model.entity.AppUser;
import space.borisgk98.kubedom.api.model.entity.ProviderNode;
import space.borisgk98.kubedom.api.repo.ProviderNodeRepo;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProviderNodeService extends AbstractCrudService<ProviderNode, Long> {

    @Autowired
    private AppUserService appUserService;

    public ProviderNodeService(JpaRepository<ProviderNode, Long> repository, EntityManager em, CriteriaBuilder cb) {
        super(repository, em, cb);
    }

    /**
     * Регистрация новой ноды либо проверка того, что нода уже существует
     * @param providerToken токен поставщика
     * @param nodeUuid        идентификатор узла (генерируется на стороне менаждера узла поставщика)
     * @return              успешная регистрация или нет
     */
    public boolean register(UUID providerToken, UUID nodeUuid) {
        if (!appUserService.existByToken(providerToken)) {
            return false;
        }
        AppUser appUser = appUserService.findByToken(providerToken).get();
        Optional<ProviderNode> existed = ((ProviderNodeRepo) repository).findByOwnerIdAndNodeUuid(appUser.getId(), nodeUuid);
        if (existed.isPresent()) {
            return true;
        }
        ProviderNode newProviderNode = ProviderNode.builder()
                .owner(appUser)
                .ownerId(appUser.getId())
                .nodeUuid(nodeUuid)
                .build();
        repository.save(newProviderNode);
        return true;
    }

    public void updateSession(UUID nodeUuid, CurrWebSocketSession webSocketSession) {
        ((ProviderNodeRepo) repository).findByNodeUuid(nodeUuid)
                .ifPresent(node -> repository.save(node.setWebSocketSession(webSocketSession)));
    }

    public void clearSession(UUID nodeUuid) {
        ((ProviderNodeRepo) repository).findByNodeUuid(nodeUuid)
                .ifPresent(node -> repository.save(node.setWebSocketSession(null)));
    }

    // TODO продвинутый поиск
    public List<ProviderNode> search(ProviderNodeSearchRequest unmap) {
        return repository.findAll();
    }
}
