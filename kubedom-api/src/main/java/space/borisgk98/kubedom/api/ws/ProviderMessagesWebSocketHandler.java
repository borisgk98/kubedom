package space.borisgk98.kubedom.api.ws;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import space.borisgk98.kubedom.api.model.enums.SessionType;
import space.borisgk98.kubedom.api.ws.handlers.HandlerDelegate;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProviderMessagesWebSocketHandler extends TextWebSocketHandler {

    private final SessionManager sessionManager;
    private final HandlerDelegate handlerDelegate;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessionManager.add(session, SessionType.PROVIDER);
        log.info("New connection (session id = {})", session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String messageAsString = (String) message.getPayload();
        log.info("Receive message {}", messageAsString);
        handlerDelegate.handle(session, messageAsString);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessionManager.remove(session, SessionType.PROVIDER);
    }
}
