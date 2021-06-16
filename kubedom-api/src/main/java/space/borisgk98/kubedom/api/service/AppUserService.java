package space.borisgk98.kubedom.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import space.borisgk98.kubedom.api.mapping.AppUserFullMapper;
import space.borisgk98.kubedom.api.mapping.CurrentUserMapper;
import space.borisgk98.kubedom.api.mapping.KubeClusterMapper;
import space.borisgk98.kubedom.api.mapping.ProviderNodeMapper;
import space.borisgk98.kubedom.api.model.dto.rest.CurrentUserDto;
import space.borisgk98.kubedom.api.model.entity.AppUser;
import space.borisgk98.kubedom.api.model.entity.CustomerNode;
import space.borisgk98.kubedom.api.model.entity.KubeCluster;
import space.borisgk98.kubedom.api.repo.ProviderRepo;
import space.borisgk98.kubedom.api.security.SecurityService;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AppUserService extends AbstractCrudService<AppUser, Long> {

    @Autowired
    private AppUserFullMapper appUserFullMapper;

    @Autowired
    private CurrentUserMapper currentUserMapper;

    @Autowired
    private ProviderNodeService providerNodeService;

    @Autowired
    private ProviderNodeMapper providerNodeMapper;

    @Autowired
    private KubeClusterService kubeClusterService;

    @Autowired
    private KubeClusterMapper kubeClusterMapper;

    @Autowired
    private SecurityService securityService;

    public AppUserService(JpaRepository<AppUser, Long> repository, EntityManager em, CriteriaBuilder cb) {
        super(repository, em, cb);
    }

    public Optional<AppUser> findByLogin(String login) {
        return ((ProviderRepo) repository).findByLogin(login);
    }

    public boolean existByLogin(String login) {
        return ((ProviderRepo) repository).existsByLogin(login);
    }

    @Override
    public AppUser create(AppUser m) {
        m.setToken(UUID.randomUUID());
        return super.create(m);
    }

    public boolean existByToken(UUID providerToken) {
        return ((ProviderRepo) repository).existsByToken(providerToken);
    }

    public Optional<AppUser> findByToken(UUID providerToken) {
        return ((ProviderRepo) repository).findByToken(providerToken);
    }

    public CurrentUserDto getCurrentUserDto() {
        var user = securityService.getCurrAppUser();
        var clusters = kubeClusterService.findByOwnerId(user.getId());
        var myNodes = providerNodeService.findByOwnerId(user.getId());
        var notMyNodes = clusters.stream()
                .map(KubeCluster::getNodes)
                .flatMap(Collection::stream)
                .map(CustomerNode::getProviderNode)
                .filter(providerNode -> !Objects.equals(providerNode.getOwnerId(), user.getId()))
                .collect(Collectors.toList());
        var currUserDto = currentUserMapper.map(appUserFullMapper.map(user));
        currUserDto.setClusters(clusters.stream().map(kubeClusterMapper::map).collect(Collectors.toList()));
        currUserDto.setMyNodes(myNodes.stream().map(providerNodeMapper::map).collect(Collectors.toList()));
        currUserDto.setNotMyNodes(notMyNodes.stream().map(providerNodeMapper::map).collect(Collectors.toList()));
        return currUserDto;
    }
}
