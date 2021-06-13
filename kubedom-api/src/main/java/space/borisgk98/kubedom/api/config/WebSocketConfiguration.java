package space.borisgk98.kubedom.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import space.borisgk98.kubedom.api.cosnt.AppConst;
import space.borisgk98.kubedom.api.cosnt.AppUrls;
import space.borisgk98.kubedom.api.ws.CustomerAuthHandshakeHandler;
import space.borisgk98.kubedom.api.ws.CustomerMessagesWebSocketHandler;
import space.borisgk98.kubedom.api.ws.ProviderAuthHandshakeHandler;
import space.borisgk98.kubedom.api.ws.ProviderMessagesWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Autowired
    private ProviderMessagesWebSocketHandler providerMessagesWebSocketHandler;

    @Autowired
    private ProviderAuthHandshakeHandler providerAuthHandshakeHandler;

    @Autowired
    private CustomerMessagesWebSocketHandler customerMessagesWebSocketHandler;

    @Autowired
    private CustomerAuthHandshakeHandler customerAuthHandshakeHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(providerMessagesWebSocketHandler, AppConst.SERVER_PREFIX + AppConst.WS + AppUrls.PROVIDER)
                .setHandshakeHandler(providerAuthHandshakeHandler);
        webSocketHandlerRegistry.addHandler(customerMessagesWebSocketHandler, AppConst.SERVER_PREFIX + AppConst.WS + AppUrls.CUSTOMER)
                .setHandshakeHandler(customerAuthHandshakeHandler);
    }

}
