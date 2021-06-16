package space.borisgk98.kubedom.api.mapping;

import org.mapstruct.Mapper;
import space.borisgk98.kubedom.api.model.dto.rest.AppUserFullDto;
import space.borisgk98.kubedom.api.model.dto.rest.CurrentUserDto;

@Mapper(componentModel = "spring")
public interface CurrentUserMapper extends IMapper<AppUserFullDto, CurrentUserDto> {
}
