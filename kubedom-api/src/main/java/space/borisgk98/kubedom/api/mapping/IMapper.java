package space.borisgk98.kubedom.api.mapping;

public interface IMapper<T, F> {
    F map(T object);
    T unmap(F object);
}
