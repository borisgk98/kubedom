package space.borisgk98.kubedom.api.model.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class CurrentUserDto {
    private Long id;

    private String login;

    private UUID token;

    private List<KubeClusterDto> clusters;

    private List<ProviderNodeDto> myNodes;

    private List<ProviderNodeDto> notMyNodes;
}
