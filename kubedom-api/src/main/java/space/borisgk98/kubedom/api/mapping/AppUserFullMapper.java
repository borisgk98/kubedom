package space.borisgk98.kubedom.api.mapping;

import org.mapstruct.Mapper;
import space.borisgk98.kubedom.api.model.dto.rest.AppUserFullDto;
import space.borisgk98.kubedom.api.model.entity.AppUser;

@Mapper(componentModel = "spring")
public interface AppUserFullMapper extends IMapper<AppUser, AppUserFullDto> {
}
