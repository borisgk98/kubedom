package space.borisgk98.kubedom.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import space.borisgk98.kubedom.api.model.dto.rest.ProviderNodeSearchRequest;
import space.borisgk98.kubedom.api.model.entity.CurrWebSocketSession;
import space.borisgk98.kubedom.api.model.entity.AppUser;
import space.borisgk98.kubedom.api.model.entity.KubeCluster;
import space.borisgk98.kubedom.api.model.entity.ProviderNode;
import space.borisgk98.kubedom.api.model.enums.ProviderNodeState;
import space.borisgk98.kubedom.api.model.enums.ProviderNodeType;
import space.borisgk98.kubedom.api.repo.ProviderNodeRepo;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
     * @param nodeId
     * @param isPrimary
     * @return              успешная регистрация или нет
     */
    public boolean register(UUID providerToken, UUID nodeUuid, String nodeId, boolean isPrimary) {
        if (!appUserService.existByToken(providerToken)) {
            return false;
        }
        AppUser appUser = appUserService.findByToken(providerToken).get();
        Optional<ProviderNode> existed = ((ProviderNodeRepo) repository).findByOwnerIdAndNodeUuid(appUser.getId(), nodeUuid);
        if (existed.isPresent()) {
            repository.save(existed.get()
                    .setType(isPrimary ? ProviderNodeType.PRIMARY : ProviderNodeType.SECONDARY)
                    .setExternalIp(nodeId));
            return true;
        }
        ProviderNode newProviderNode = ProviderNode.builder()
                .owner(appUser)
                .ownerId(appUser.getId())
                .nodeUuid(nodeUuid)
                .externalIp(nodeId)
                .type(isPrimary ? ProviderNodeType.PRIMARY : ProviderNodeType.SECONDARY)
                .build();
        repository.save(newProviderNode);
        return true;
    }

    public void updateSession(UUID nodeUuid, CurrWebSocketSession webSocketSession) {
        ((ProviderNodeRepo) repository).findByNodeUuid(nodeUuid)
                .ifPresent(node -> repository.save(node
                        .setWebSocketSession(webSocketSession)
                        .setProviderNodeState(ProviderNodeState.ACTIVE)));
    }

    public void clearSession(UUID nodeUuid) {
        ((ProviderNodeRepo) repository).findByNodeUuid(nodeUuid)
                .ifPresent(node -> repository.save(node
                        .setWebSocketSession(null)
                        .setProviderNodeState(ProviderNodeState.DETACHED)));
    }

    // TODO продвинутый поиск
    public List<ProviderNode> search(ProviderNodeSearchRequest searchRequest) {
        return repository.findAll().stream()
                .filter(node -> Objects.equals(node.getType(), searchRequest.getProviderNodeType()))
                .filter(node -> Objects.equals(node.getProviderNodeState(), searchRequest.getProviderNodeState()))
                .collect(Collectors.toList());
    }

    public List<ProviderNode> findByOwnerId(Long userId) {
        return ((ProviderNodeRepo) repository).findAllByOwnerId(userId);
    }
}
