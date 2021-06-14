package space.borisgk98.kubedom.api.mapping;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import space.borisgk98.kubedom.api.model.dto.rest.AppUserFullDto;
import space.borisgk98.kubedom.api.model.entity.AppUser;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-06-14T23:03:57+0300",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.0.2.jar, environment: Java 15.0.2 (N/A)"
)
@Component
public class AppUserFullMapperImpl implements AppUserFullMapper {

    @Override
    public AppUserFullDto map(AppUser object) {
        if ( object == null ) {
            return null;
        }

        AppUserFullDto appUserFullDto = new AppUserFullDto();

        appUserFullDto.setId( object.getId() );
        appUserFullDto.setLogin( object.getLogin() );
        appUserFullDto.setToken( object.getToken() );

        return appUserFullDto;
    }

    @Override
    public AppUser unmap(AppUserFullDto object) {
        if ( object == null ) {
            return null;
        }

        AppUser appUser = new AppUser();

        appUser.setId( object.getId() );
        appUser.setLogin( object.getLogin() );
        appUser.setToken( object.getToken() );

        return appUser;
    }
}
