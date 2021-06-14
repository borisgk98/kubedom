package space.borisgk98.kubedom.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.borisgk98.kubedom.api.cosnt.AppConst;
import space.borisgk98.kubedom.api.cosnt.AppUrls;
import space.borisgk98.kubedom.api.model.dto.rest.CustomerNodeCreationRequest;
import space.borisgk98.kubedom.api.service.CustomerNodeService;

@RestController
@RequestMapping(AppConst.SERVER_PREFIX + AppUrls.CUSTOMER_NODE)
@RequiredArgsConstructor
public class CustomerNodeController {

    private final CustomerNodeService customerNodeService;

//    @PostMapping
//    public void create(@RequestBody CustomerNodeCreationRequest dto) {
//        customerNodeService.createPending(dto);
//    }
//
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable(name = "id") Long customerNodeId) {
//        customerNodeService.delete(customerNodeId);
//    }
}
