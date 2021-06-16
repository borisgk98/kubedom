package space.borisgk98.kubedom.api.model.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import space.borisgk98.kubedom.api.model.enums.KubeClusterStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class KubeClusterDto {
    private Long id;

    private List<CustomerNodeDto> nodes;

    private AppUserFullDto owner;

    private Long ownerId;

    private KubeClusterStatus status;
}
