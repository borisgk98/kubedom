package space.borisgk98.kubedom.api.model.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import space.borisgk98.kubedom.api.model.enums.ProviderNodeState;
import space.borisgk98.kubedom.api.model.enums.ProviderNodeType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ProviderNodeSearchRequest {
    private ProviderNodeState providerNodeState;
    private ProviderNodeType providerNodeType;
}
