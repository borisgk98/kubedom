package space.borisgk98.kubedom.api.ws.handlers.customer;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import space.borisgk98.kubedom.api.model.enums.CustomerNodeType;
import space.borisgk98.kubedom.api.model.enums.WSMessageType;
import space.borisgk98.kubedom.api.service.CustomerNodeService;
import space.borisgk98.kubedom.api.ws.handlers.IHandler;

@Service
@RequiredArgsConstructor
public class WorkerCreationResponseHandler implements IHandler {
    private final CustomerNodeService customerNodeService;

    @Override
    @SneakyThrows
    public void handle(WebSocketSession session, String data) {
        var customerNode = customerNodeService.getBySessionId(session.getId());
        customerNode
                .setType(CustomerNodeType.WORKER)
                .setReady(true);
        customerNodeService.update(customerNode);
    }

    @Override
    public WSMessageType type() {
        return WSMessageType.K3S_WORKER_CREATION_RESPONSE;
    }
}
