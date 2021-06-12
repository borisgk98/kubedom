package space.borisgk98.kubedom.api.mapping;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import space.borisgk98.kubedom.api.model.dto.rest.CustomerNodeCreationRequest;
import space.borisgk98.kubedom.api.model.dto.rest.ProviderNodeSearchRequest;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-06-13T00:24:12+0300",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.0.2.jar, environment: Java 15.0.2 (N/A)"
)
@Component
public class ProviderNodeSearchMapperImpl implements ProviderNodeSearchMapper {

    @Override
    public CustomerNodeCreationRequest map(ProviderNodeSearchRequest object) {
        if ( object == null ) {
            return null;
        }

        CustomerNodeCreationRequest customerNodeCreationRequest = new CustomerNodeCreationRequest();

        return customerNodeCreationRequest;
    }

    @Override
    public ProviderNodeSearchRequest unmap(CustomerNodeCreationRequest object) {
        if ( object == null ) {
            return null;
        }

        ProviderNodeSearchRequest providerNodeSearchRequest = new ProviderNodeSearchRequest();

        return providerNodeSearchRequest;
    }
}
