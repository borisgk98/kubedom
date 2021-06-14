package space.borisgk98.kubedom.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.borisgk98.kubedom.api.cosnt.AppConst;
import space.borisgk98.kubedom.api.cosnt.AppUrls;
import space.borisgk98.kubedom.api.service.KubeClusterService;

@RestController
@RequestMapping(AppConst.SERVER_PREFIX + AppUrls.KUBE_CLUSTER)
@RequiredArgsConstructor
public class KubeClusterController {

    private final KubeClusterService kubeClusterService;

//    // TODO more parameters
    @PostMapping
    public void create() {
        kubeClusterService.create();
    }
}
