package space.borisgk98.kubedom.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.borisgk98.kubedom.api.cosnt.AppConst;
import space.borisgk98.kubedom.api.exception.ModelNotFound;
import space.borisgk98.kubedom.api.model.dto.rest.ProviderNodeSearchRequest;
import space.borisgk98.kubedom.api.model.dto.ws.WSCustomerNodeCreationDto;
import space.borisgk98.kubedom.api.model.entity.ProviderNode;
import space.borisgk98.kubedom.api.service.ProviderNodeService;
import space.borisgk98.kubedom.api.ws.WebSocketSender;

@RestController
@RequestMapping(AppConst.SERVER_PREFIX + "/test")
@RequiredArgsConstructor
public class TestController {
    @Autowired
    private ProviderNodeService providerNodeService;
    @Autowired
    private WebSocketSender webSocketSender;

    @Value("${app.ova-location}")
    private String ovaLocation;

    @GetMapping
    public ResponseEntity check() {
        ProviderNode providerNode = providerNodeService.search(new ProviderNodeSearchRequest())
                .stream().findFirst()
                .orElseThrow(ModelNotFound::new);
        var customerNodeCreationDto = new WSCustomerNodeCreationDto();
        customerNodeCreationDto.setOvaLocation(ovaLocation);
        webSocketSender.send(providerNode.getWebSocketSessionId(), customerNodeCreationDto);
        return ResponseEntity.ok("");
    }
}
