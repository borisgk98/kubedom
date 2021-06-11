package space.borisgk98.kubedom.api.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import space.borisgk98.kubedom.api.model.dto.ws.WSCustomerNodeCreationDto;

@Getter
@AllArgsConstructor
public enum WSMessageType {
    ERROR(Exception.class),
    CUSTOMER_NODE_CREATION(WSCustomerNodeCreationDto.class);

    private Class mClass;

    public static WSMessageType fromClass(Class c) {
        for (WSMessageType wsMessageType : values()) {
            if (wsMessageType.getMClass().equals(c)) {
                return wsMessageType;
            }
        }
        return null;
    }
}
