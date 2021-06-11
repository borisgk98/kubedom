package space.borisgk98.kubedom.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.borisgk98.kubedom.api.model.dto.ws.CustomerNodeCreationDto;
import space.borisgk98.kubedom.api.model.entity.ProviderNode;
import space.borisgk98.kubedom.api.ws.WebSocketSender;

@Service
@RequiredArgsConstructor
public class CustomerNodeService {
    private final ProviderNodeService providerNodeService;
    private final WebSocketSender webSocketSender;

    public void create(Long providerNodeId) {
        ProviderNode providerNode = providerNodeService.read(providerNodeId);
        if (providerNode.getWebSocketSession() == null) {
            return;
        }
        webSocketSender.send(providerNode.getWebSocketSessionId(), new CustomerNodeCreationDto());
    }
}
