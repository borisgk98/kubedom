package space.borisgk98.kubedom.api.mapping;


import org.mapstruct.Mapper;
import space.borisgk98.kubedom.api.model.dto.rest.ProviderNodeDto;
import space.borisgk98.kubedom.api.model.entity.ProviderNode;

@Mapper(componentModel = "spring", uses = AppUserFullMapper.class)
public interface ProviderNodeMapper extends IMapper<ProviderNode, ProviderNodeDto> {
}
