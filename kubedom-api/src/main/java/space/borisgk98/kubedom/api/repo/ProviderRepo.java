package space.borisgk98.kubedom.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import space.borisgk98.kubedom.api.model.entity.AppUser;

import java.util.Optional;
import java.util.UUID;

public interface ProviderRepo extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByLogin(String login);

    boolean existsByLogin(String login);

//    @Query("select p.id from Provider p where p.token = :token")
    boolean existsByToken(UUID token);

    Optional<AppUser> findByToken(UUID token);
}
