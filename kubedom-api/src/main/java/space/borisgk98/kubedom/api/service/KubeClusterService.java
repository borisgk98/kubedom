package space.borisgk98.kubedom.api.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import space.borisgk98.kubedom.api.model.dto.rest.CustomerNodeCreationRequest;
import space.borisgk98.kubedom.api.model.dto.ws.WSK3sMasterCreationDto;
import space.borisgk98.kubedom.api.model.enums.CustomerNodeState;
import space.borisgk98.kubedom.api.repo.KubeClusterRepo;
import space.borisgk98.kubedom.api.ws.WebSocketSender;

import java.util.Objects;

import static java.lang.Thread.sleep;

@Service
@RequiredArgsConstructor
@Slf4j
public class KubeClusterService {

    private final KubeClusterRepo kubeClusterRepo;
    private final CustomerNodeService customerNodeService;
    private final WebSocketSender webSocketSender;

    @SneakyThrows
    public void create() {
        var customerNode = customerNodeService.create(CustomerNodeCreationRequest.builder().build());
        // TODO нормальная реализациия метода customerNodeService.create через отправку ответа при успешном создании ноды
        while (!Objects.equals(customerNodeService.read(customerNode.getId()).getCustomerNodeState(), CustomerNodeState.ACTIVE)) {
            log.info("Waiting node ready");
            sleep(500);
        }
        webSocketSender.send(customerNode.getWebSocketSessionId(), new WSK3sMasterCreationDto());
    }

    @SneakyThrows
    public void createTest() {
        Long nodeId = 8L;
        var customerNode = customerNodeService.read(nodeId);
        // TODO нормальная реализациия метода customerNodeService.create через отправку ответа при успешном создании ноды
        while (!Objects.equals(customerNodeService.read(customerNode.getId()).getCustomerNodeState(), CustomerNodeState.ACTIVE)) {
            log.info("Waiting node ready");
            sleep(500);
        }
        webSocketSender.send(
                customerNode.getWebSocketSessionId(),
                new WSK3sMasterCreationDto()
                        .setExternalIp(customerNode.getProviderNode().getExternalIp())
        );
    }
}
