package space.borisgk98.kubedom.api.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class CreateCustomerNodeDto {
    private Integer cpuCount;
    private String ovaUrl;
    private String machineName;
}
