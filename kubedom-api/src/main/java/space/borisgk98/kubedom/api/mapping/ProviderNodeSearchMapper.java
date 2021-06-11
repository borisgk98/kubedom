package space.borisgk98.kubedom.api.mapping;

import org.mapstruct.Mapper;
import space.borisgk98.kubedom.api.model.dto.rest.CustomerNodeCreationRequest;
import space.borisgk98.kubedom.api.model.dto.rest.ProviderNodeSearchRequest;

@Mapper(componentModel = "spring")
public interface ProviderNodeSearchMapper extends IMapper<ProviderNodeSearchRequest, CustomerNodeCreationRequest> {
    @Override
    CustomerNodeCreationRequest map(ProviderNodeSearchRequest object);

    @Override
    ProviderNodeSearchRequest unmap(CustomerNodeCreationRequest object);
}
