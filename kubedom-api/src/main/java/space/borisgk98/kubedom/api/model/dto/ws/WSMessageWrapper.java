package space.borisgk98.kubedom.api.model.dto.ws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import space.borisgk98.kubedom.api.model.enums.WSMessageType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WSMessageWrapper {
    private WSMessageType type;
    private String data;
}
