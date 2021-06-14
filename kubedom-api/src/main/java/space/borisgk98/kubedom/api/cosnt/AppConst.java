package space.borisgk98.kubedom.api.cosnt;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppConst {
    public static final String SERVER_PREFIX = "/api/kubedom";
    public static final String AUTH_HEADER = "Authorization";
    public static final String PROVIDER_NODE_AUTH_HEADER = "Provider-node-token";
    public static final String PROVIDER_NODE_DEVICE_HEADER = "Provider-node-device-uuid";
    public static final String PROVIDER_NODE_IP_HEADER = "Provider-node-external-ip";
    public static final String PROVIDER_NODE_IS_PRIMARY = "Provider-node-is-primary";
    public static final String CUSTOMER_NODE_DEVICE_HEADER = "Customer-node-device-uuid";
    public static final String WS = "/ws";
}
