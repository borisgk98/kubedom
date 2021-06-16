package space.borisgk98.kubedom.api.mapping;

import org.mapstruct.Mapper;
import space.borisgk98.kubedom.api.model.dto.rest.KubeClusterDto;
import space.borisgk98.kubedom.api.model.entity.KubeCluster;

@Mapper(componentModel = "spring", uses = { AppUserFullMapper.class, CustomerNodeMapper.class, ProviderNodeMapper.class })
public interface KubeClusterMapper extends IMapper<KubeCluster, KubeClusterDto> {
}
