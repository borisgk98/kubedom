package space.borisgk98.kubedom.api.model.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import space.borisgk98.kubedom.api.model.enums.CustomerNodeState;
import space.borisgk98.kubedom.api.model.enums.CustomerNodeType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CustomerNodeDto {

    private Long id;

    private CustomerNodeType type;

    private AppUserFullDto owner;

    private Long ownerId;

    private Long providerNodeId;

    private CustomerNodeState customerNodeState;

    private boolean ready = false;

    private String machineName;

    private String masterToken;

    private String kubectlConfig;

    private Long kubeClusterId;
}
