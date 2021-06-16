package space.borisgk98.kubedom.api.scheduling;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import space.borisgk98.kubedom.api.model.dto.ws.WSK3sMasterCreationDto;
import space.borisgk98.kubedom.api.model.dto.ws.WSK3sWorkerCreationDto;
import space.borisgk98.kubedom.api.repo.CustomerNodeRepo;
import space.borisgk98.kubedom.api.ws.WebSocketSender;

@Service
@RequiredArgsConstructor
public class K3sDeployingScheduler {

    private final CustomerNodeRepo customerNodeRepo;
    private final WebSocketSender webSocketSender;

    // 30 sec
    @Scheduled(fixedDelay = 30 * 1000)
    public void deployK3sMaster() {
        customerNodeRepo.findWaitingMasters().parallelStream().forEach(masterNode -> {
            webSocketSender.send(
                    masterNode.getWebSocketSessionId(),
                    new WSK3sMasterCreationDto()
                            .setExternalIp(masterNode.getProviderNode().getExternalIp())
                            .setNodeName(String.format("master-%d", masterNode.getId()))
            );
        });
    }

    // 30 sec
    @Scheduled(fixedDelay = 30 * 1000)
    public void deployK3sWorker() {
        customerNodeRepo.findWaitingWorkers().parallelStream().forEach(workerNode -> {
            var storedMaster = customerNodeRepo.findReadyMasterByClusterId(workerNode.getKubeClusterId());
            if (storedMaster.isEmpty()) {
                return;
            }
            webSocketSender.send(
                    workerNode.getWebSocketSessionId(),
                    new WSK3sWorkerCreationDto()
                            .setMasterExternalIp(storedMaster.get().getProviderNode().getExternalIp())
                            .setMasterToken(storedMaster.get().getMasterToken())
                            .setNodeName(String.format("worker-%d", workerNode.getId()))
            );
        });
    }

}
