package space.borisgk98.kubedom.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import space.borisgk98.kubedom.api.model.entity.Provider;

import java.util.Optional;
import java.util.UUID;

public interface ProviderRepo extends JpaRepository<Provider, Long> {

    Optional<Provider> findByLogin(String login);

    boolean existsByLogin(String login);

//    @Query("select p.id from Provider p where p.token = :token")
    boolean existsByToken(UUID token);

    Optional<Provider> findByToken(UUID token);
}
