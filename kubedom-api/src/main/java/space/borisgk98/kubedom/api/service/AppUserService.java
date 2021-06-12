package space.borisgk98.kubedom.api.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import space.borisgk98.kubedom.api.model.entity.AppUser;
import space.borisgk98.kubedom.api.repo.ProviderRepo;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppUserService extends AbstractCrudService<AppUser, Long> {
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
}