package space.borisgk98.kubedom.api.service;

import space.borisgk98.kubedom.api.exception.ModelNotFound;
import space.borisgk98.kubedom.api.model.entity.IEntity;

import java.util.List;

public interface ICrudService<T extends IEntity, F> {
    T create(T m);
    T read(F id) throws ModelNotFound;
    T update(T m) throws ModelNotFound;
    void delete(F id) throws ModelNotFound;
    boolean existById(F id);
    boolean exist(T m);
    List<T> getAll();
    List<T> getRange(Integer offset, Integer limit);
}