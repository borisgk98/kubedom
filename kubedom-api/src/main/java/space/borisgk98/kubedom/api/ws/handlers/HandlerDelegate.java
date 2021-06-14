package space.borisgk98.kubedom.api.ws.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import space.borisgk98.kubedom.api.model.dto.ws.WSMessageWrapper;
import space.borisgk98.kubedom.api.model.enums.WSMessageType;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HandlerDelegate {

    private final List<IHandler> handlers;
    private final ObjectMapper objectMapper;

    private Map<WSMessageType, IHandler> handlerMap;

    @PostConstruct
    private void postConstruct() {
        handlerMap = handlers.stream().collect(Collectors.toUnmodifiableMap(IHandler::type, Function.identity()));
    }

    @SneakyThrows
    public void handle(WebSocketSession session, String message) {
        WSMessageWrapper messageWrapper = objectMapper.readValue(message, WSMessageWrapper.class);
        Optional.ofNullable(handlerMap.get(messageWrapper.getType()))
                .ifPresent(iHandler -> iHandler.handle(session, messageWrapper.getData()));
    }

}
