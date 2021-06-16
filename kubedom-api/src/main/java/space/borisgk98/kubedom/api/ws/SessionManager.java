package space.borisgk98.kubedom.api.ws;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import space.borisgk98.kubedom.api.cosnt.AppConst;
import space.borisgk98.kubedom.api.model.entity.CurrWebSocketSession;
import space.borisgk98.kubedom.api.model.enums.SessionType;
import space.borisgk98.kubedom.api.service.CustomerNodeService;
import space.borisgk98.kubedom.api.service.ProviderNodeService;
import space.borisgk98.kubedom.api.service.SessionService;
import space.borisgk98.kubedom.api.utils.HttpUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionManager {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private ProviderNodeService providerNodeService;
    @Autowired
    private CustomerNodeService customerNodeService;

    private static Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Transactional
    public void add(WebSocketSession session, SessionType sessionType) {
        sessions.put(session.getId(), session);
        CurrWebSocketSession newSession = sessionService.create(new CurrWebSocketSession().setId(session.getId()));
        switch (sessionType) {
            case PROVIDER:
                HttpUtils.getHeader(session.getHandshakeHeaders(), AppConst.PROVIDER_NODE_DEVICE_HEADER)
                    .map(UUID::fromString)
                    .ifPresent(nodeUuid -> providerNodeService.updateSession(nodeUuid, newSession));
                break;
            case CUSTOMER:
                HttpUtils.getHeader(session.getHandshakeHeaders(), AppConst.CUSTOMER_NODE_DEVICE_HEADER)
                        .map(Long::valueOf)
                        .ifPresent(nodeUuid -> customerNodeService.updateSession(nodeUuid, newSession));
                break;
        }
    }


    @Transactional
    public void remove(WebSocketSession session, SessionType sessionType) {
        sessions.remove(session.getId());
        switch (sessionType) {
            case PROVIDER:
                HttpUtils.getHeader(session.getHandshakeHeaders(), AppConst.PROVIDER_NODE_DEVICE_HEADER)
                        .map(UUID::fromString)
                        .ifPresent(providerNodeService::clearSession);
                break;
            case CUSTOMER:
                HttpUtils.getHeader(session.getHandshakeHeaders(), AppConst.CUSTOMER_NODE_DEVICE_HEADER)
                        .map(Long::valueOf)
                        .ifPresent(customerNodeService::clearSession);
                break;
        }
        sessionService.removeById(session.getId());
    }

    public Optional<WebSocketSession> get(String sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }

    public Collection<WebSocketSession> getAll() {
        return sessions.values();
    }
}
