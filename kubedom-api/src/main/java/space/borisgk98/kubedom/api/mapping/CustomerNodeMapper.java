package space.borisgk98.kubedom.api.mapping;

import org.mapstruct.Mapper;
import space.borisgk98.kubedom.api.model.dto.rest.CustomerNodeDto;
import space.borisgk98.kubedom.api.model.entity.CustomerNode;

@Mapper(componentModel = "spring", uses = AppUserFullMapper.class)
public interface CustomerNodeMapper extends IMapper<CustomerNode, CustomerNodeDto> {
}
