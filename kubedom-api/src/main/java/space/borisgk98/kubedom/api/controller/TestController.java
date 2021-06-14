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
import space.borisgk98.kubedom.api.model.entity.CustomerNode;
import space.borisgk98.kubedom.api.model.entity.ProviderNode;
import space.borisgk98.kubedom.api.service.CustomerNodeService;
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
    @Autowired
    private CustomerNodeService customerNodeService;

    @Value("${app.ova-location}")
    private String ovaLocation;

    @GetMapping
    public void check() {
        CustomerNode worker = customerNodeService.read(8L);
        CustomerNode master = customerNodeService.read(1L);
        customerNodeService.deployK3sWorker(worker, master);
    }
}
