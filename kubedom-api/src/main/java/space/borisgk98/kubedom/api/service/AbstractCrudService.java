package space.borisgk98.kubedom.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import space.borisgk98.kubedom.api.exception.ModelNotFound;
import space.borisgk98.kubedom.api.model.entity.IEntity;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class AbstractCrudService<T extends IEntity<F>, F> implements ICrudService<T, F> {
    protected final JpaRepository<T, F> repository;
    protected final EntityManager em;
    protected final CriteriaBuilder cb;

    @Override
    public T create(T m) {
        return repository.save(m);
    }

    @Override
    public T read(F id) throws ModelNotFound {
        Optional<T> m = repository.findById(id);
        if (!m.isPresent()) {
            throw new ModelNotFound();
        }
        return m.get();
    }

    @Override
    public T update(T m) throws ModelNotFound {
        if (!existById(m.getId())) {
            throw new ModelNotFound();
        }
        return repository.save(m);
    }

    @Override
    public void delete(F id) throws ModelNotFound {
        if (!existById(id)) {
            throw new ModelNotFound();
        }
        repository.deleteById(id);
    }

    @Override
    public boolean exist(T m) {
        return repository.exists(Example.of(m));
    }

    @Override
    public boolean existById(F id) {
        return repository.existsById(id);
    }

    @Override
    public List<T> getAll() {
        return repository.findAll();
    }

    @Override
    public List<T> getRange(Integer offset, Integer limit) {
        Pageable request = PageRequest.of(offset, limit);
        return repository.findAll(request).getContent();
    }
}