package space.borisgk98.kubedom.api.model.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import space.borisgk98.kubedom.api.model.enums.ProviderNodeState;
import space.borisgk98.kubedom.api.model.enums.ProviderNodeType;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ProviderNodeDto {

    private Long id;

    private AppUserFullDto owner;

    private Long ownerId;

    private UUID nodeUuid;

    private ProviderNodeState providerNodeState;

    private String externalIp;

    private ProviderNodeType type;
}
