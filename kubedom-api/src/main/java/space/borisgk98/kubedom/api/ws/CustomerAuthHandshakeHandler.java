package space.borisgk98.kubedom.api.ws;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import space.borisgk98.kubedom.api.cosnt.AppConst;
import space.borisgk98.kubedom.api.service.CustomerNodeService;
import space.borisgk98.kubedom.api.utils.HttpUtils;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerAuthHandshakeHandler implements HandshakeHandler {

    private final CustomerNodeService customerNodeService;

    private DefaultHandshakeHandler handshakeHandler = new DefaultHandshakeHandler();

    @Override
    public boolean doHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws HandshakeFailureException {
        Optional<Long> nodeId = HttpUtils.getHeader(serverHttpRequest, AppConst.CUSTOMER_NODE_DEVICE_HEADER).map(Long::valueOf);
        if (nodeId.isPresent() && customerNodeService.register(nodeId.get())) {
            return handshakeHandler.doHandshake(serverHttpRequest, serverHttpResponse, webSocketHandler, map);
        } else {
            serverHttpResponse.setStatusCode(HttpStatus.FORBIDDEN);
            return false;
        }
    }
}
