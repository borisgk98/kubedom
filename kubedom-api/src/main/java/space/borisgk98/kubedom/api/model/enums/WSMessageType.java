package space.borisgk98.kubedom.api.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import space.borisgk98.kubedom.api.model.dto.ws.WSCustomerNodeCreationDto;
import space.borisgk98.kubedom.api.model.dto.ws.WSCustomerNodeRemovingDto;
import space.borisgk98.kubedom.api.model.dto.ws.WSK3MasterCreationResponseDto;
import space.borisgk98.kubedom.api.model.dto.ws.WSK3sMasterCreationDto;

@Getter
@AllArgsConstructor
public enum WSMessageType {
    ERROR(Exception.class),
    CUSTOMER_NODE_CREATION(WSCustomerNodeCreationDto.class),
    CUSTOMER_NODE_REMOVING(WSCustomerNodeRemovingDto.class),
    K3S_MASTER_CREATION(WSK3sMasterCreationDto.class),
    K3S_MASTER_CREATION_RESPONSE(WSK3MasterCreationResponseDto.class);

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
