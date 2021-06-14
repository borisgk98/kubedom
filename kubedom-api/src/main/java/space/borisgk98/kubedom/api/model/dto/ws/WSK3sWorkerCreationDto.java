package space.borisgk98.kubedom.api.model.dto.ws;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class WSK3sWorkerCreationDto {
    private String masterExternalIp;
    private String masterToken;
}