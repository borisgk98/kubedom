package space.borisgk98.kubedom.api.ws.handlers;

import org.springframework.web.socket.WebSocketSession;
import space.borisgk98.kubedom.api.model.enums.WSMessageType;

public interface IHandler {
    void handle(WebSocketSession session, String data);
    WSMessageType type();
}
