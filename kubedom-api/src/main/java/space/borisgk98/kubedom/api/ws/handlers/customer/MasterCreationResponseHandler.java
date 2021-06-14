package space.borisgk98.kubedom.api.ws.handlers.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import space.borisgk98.kubedom.api.model.dto.ws.WSK3MasterCreationResponseDto;
import space.borisgk98.kubedom.api.model.entity.CustomerNode;
import space.borisgk98.kubedom.api.model.entity.ProviderNode;
import space.borisgk98.kubedom.api.model.enums.CustomerNodeType;
import space.borisgk98.kubedom.api.model.enums.WSMessageType;
import space.borisgk98.kubedom.api.service.CustomerNodeService;
import space.borisgk98.kubedom.api.ws.handlers.IHandler;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MasterCreationResponseHandler implements IHandler {

    private final ObjectMapper objectMapper;
    private final CustomerNodeService customerNodeService;

    @Override
    @SneakyThrows
    public void handle(WebSocketSession session, String data) {
        var dto = objectMapper.readValue(data, WSK3MasterCreationResponseDto.class);
        CustomerNode customerNode = customerNodeService.getBySessionId(session.getId());
        ProviderNode providerNode = customerNode.getProviderNode();
        String kubeConfig = replaceKubeApiServerIp(dto.getKubectlConfig(), providerNode.getExternalIp());
//        String kubeConfig = dto.getKubectlConfig();
        customerNode
                .setKubectlConfig(kubeConfig)
                .setMasterToken(dto.getToken())
                .setType(CustomerNodeType.MASTER);
        customerNodeService.update(customerNode);
    }

    private String replaceKubeApiServerIp(String originalKubeConfig, String ip) {
        String serverString = String.format("server: https://%s:6443", ip);
        Pattern p = Pattern.compile("server: https://[.\\d]+:6443",
                Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        return p.matcher(originalKubeConfig).replaceAll(serverString);
    }

    @Override
    public WSMessageType type() {
        return WSMessageType.K3S_MASTER_CREATION_RESPONSE;
    }
}
