package space.borisgk98.kubedom.api.ws;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import space.borisgk98.kubedom.api.cosnt.AppConst;
import space.borisgk98.kubedom.api.model.entity.CurrWebSocketSession;
import space.borisgk98.kubedom.api.service.ProviderNodeService;
import space.borisgk98.kubedom.api.service.SessionService;
import space.borisgk98.kubedom.api.utils.HttpUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SessionManager {

    private final SessionService sessionService;
    private final ProviderNodeService providerNodeService;

    private static Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void add(WebSocketSession session) {
        sessions.put(session.getId(), session);
        CurrWebSocketSession newSession = sessionService.create(new CurrWebSocketSession().setId(session.getId()));
        HttpUtils.getHeader(session.getHandshakeHeaders(), AppConst.PROVIDER_NODE_DEVICE_HEADER)
                .map(UUID::fromString)
                .ifPresent(nodeUuid -> providerNodeService.updateSession(nodeUuid, newSession));
    }

    public void remove(WebSocketSession session) {
        sessions.remove(session.getId());
        HttpUtils.getHeader(session.getHandshakeHeaders(), AppConst.PROVIDER_NODE_DEVICE_HEADER)
                .map(UUID::fromString)
                .ifPresent(providerNodeService::clearSession);
        sessionService.removeById(session.getId());
    }

    public Optional<WebSocketSession> get(String sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }

    public Collection<WebSocketSession> getAll() {
        return sessions.values();
    }
}
