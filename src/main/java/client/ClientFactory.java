package client;

import config.Global;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.UnknownHostException;

/**
 * @author Roger
 * @date 2018/1/20.
 */
public class ClientFactory {

    private volatile static Client client;
    private static final Settings settings = Settings.builder()
            .put("cluster.name", Global.getEsClusterName())
            .put("client.transport.sniff", true)
            // .put("xpack.security.user", "elastic:irlab&elastic")
            .build();

    public static Client get() {
        if (client == null) {
            synchronized (ClientFactory.class) {
                if (client == null) {
                    try {
                        client = new PreBuiltTransportClient(settings)
                                .addTransportAddresses(Global.getTransportAddresses());
                        // client = new PreBuiltXPackTransportClient(settings)
                        //         .addTransportAddresses(Global.getTransportAddresses());
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return client;
    }
}
