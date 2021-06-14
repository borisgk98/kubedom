package space.borisgk98.kubedom.api.ws;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.util.WebUtils;
import space.borisgk98.kubedom.api.cosnt.AppConst;
import space.borisgk98.kubedom.api.service.ProviderNodeService;
import space.borisgk98.kubedom.api.utils.HttpUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class ProviderAuthHandshakeHandler implements HandshakeHandler {

    private final ProviderNodeService providerNodeService;

    private DefaultHandshakeHandler handshakeHandler = new DefaultHandshakeHandler();

    @Override
    public boolean doHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws HandshakeFailureException {
        Optional<UUID> token = HttpUtils.getHeader(serverHttpRequest, AppConst.PROVIDER_NODE_AUTH_HEADER).map(UUID::fromString);
        Optional<UUID> nodeUuid = HttpUtils.getHeader(serverHttpRequest, AppConst.PROVIDER_NODE_DEVICE_HEADER).map(UUID::fromString);
        Optional<String> nodeIp = HttpUtils.getHeader(serverHttpRequest, AppConst.PROVIDER_NODE_IP_HEADER);
        if (token.isPresent() && nodeUuid.isPresent() && nodeIp.isPresent() && providerNodeService.register(token.get(), nodeUuid.get(), nodeIp.get())) {
            return handshakeHandler.doHandshake(serverHttpRequest, serverHttpResponse, webSocketHandler, map);
        } else {
            serverHttpResponse.setStatusCode(HttpStatus.FORBIDDEN);
            return false;
        }
    }
}
