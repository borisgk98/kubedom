package space.borisgk98.kubedom.api.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import space.borisgk98.kubedom.api.model.dto.ws.WSMessageWrapper;
import space.borisgk98.kubedom.api.model.enums.WSMessageType;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketSender {

    private final SessionManager sessionManager;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public <T> void sendAll(T dto) {
        for (var session : sessionManager.getAll()) {
            session.sendMessage(new TextMessage(convert(dto)));
        }
    }

    @SneakyThrows
    public <T> void send(String sessionId, T dto) {
        if (sessionManager.get(sessionId).isPresent()) {
            sessionManager.get(sessionId).get().sendMessage(new TextMessage(convert(dto)));
        }
    }

    // TODO обработка исключений
    @SneakyThrows
    private <T> String convert(T dto) {
        var type = WSMessageType.fromClass(dto.getClass());
        var data = objectMapper.writeValueAsString(dto);
        var wrapper = new WSMessageWrapper(type, data);
        return objectMapper.writeValueAsString(wrapper);
    }
}
