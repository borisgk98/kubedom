package space.borisgk98.kubedom.api.ws.handlers;

import space.borisgk98.kubedom.api.model.enums.WSMessageType;

public interface IHandler {
    void handle(String data);
    WSMessageType type();
}
