package space.borisgk98.kubedom.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.borisgk98.kubedom.api.service.CustomerNodeService;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final CustomerNodeService customerNodeService;

    @GetMapping("/{id}")
    public void test(@PathVariable("id") Long providerNodeId) {
        customerNodeService.create(providerNodeId);
    }
}
