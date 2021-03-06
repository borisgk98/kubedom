package space.borisgk98.kubedom.api.model.dto.ws;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class WSCustomerNodeCreationDto {
    private Integer cpuCount;
    private String machineName;
    private String ovaLocation;
    private String customerNodeConfig;
}
