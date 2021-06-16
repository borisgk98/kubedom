package space.borisgk98.kubedom.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.borisgk98.kubedom.api.cosnt.AppConst;
import space.borisgk98.kubedom.api.cosnt.AppUrls;
import space.borisgk98.kubedom.api.mapping.KubeClusterMapper;
import space.borisgk98.kubedom.api.model.dto.rest.ClusterCreationRequest;
import space.borisgk98.kubedom.api.model.dto.rest.KubeClusterDto;
import space.borisgk98.kubedom.api.service.KubeClusterService;

@RestController
@RequestMapping(AppConst.SERVER_PREFIX + AppUrls.KUBE_CLUSTER)
@RequiredArgsConstructor
public class KubeClusterController {

    private final KubeClusterService kubeClusterService;
    private final KubeClusterMapper kubeClusterMapper;

//    // TODO more parameters
    @PostMapping
    public KubeClusterDto create(@RequestBody ClusterCreationRequest clusterCreationRequest) {
        return kubeClusterMapper.map(kubeClusterService.create(clusterCreationRequest));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        kubeClusterService.delete(id);
    }

    @GetMapping("/{id}")
    public KubeClusterDto get(@PathVariable("id") Long id) {
        return kubeClusterMapper.map(kubeClusterService.read(id));
    }
}
