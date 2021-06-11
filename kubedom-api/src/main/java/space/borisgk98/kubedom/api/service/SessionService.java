package space.borisgk98.kubedom.api.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import space.borisgk98.kubedom.api.model.entity.CurrWebSocketSession;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;

@Service
public class SessionService extends AbstractCrudService<CurrWebSocketSession, String> {
    public SessionService(JpaRepository<CurrWebSocketSession, String> repository, EntityManager em, CriteriaBuilder cb) {
        super(repository, em, cb);
    }

    public void removeById(String sessionId) {
        repository.deleteById(sessionId);
    }
}
