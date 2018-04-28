package config;

import org.elasticsearch.common.transport.TransportAddress;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * @author Roger
 * @date 2018/1/20.
 */
public class Global {
    private static final String APPLICATION_PROPERTIES = "application.properties";
    private static final String esClusterName = "es.cluster.name";
    private static final String esTransportAddress = "es.transport.address";
    private static final String esNumOfShards = "es.num.shards";
    private static final String esNumOfReplicas = "es.num.replicas";
    private static final String esBulkActions = "es.bulk.actions";
    private static final String esBulkSize = "es.bulk.size";


    public static final String ncitationThreshold = "ncitation.threshold";

    private static Properties props = new Properties();
    static {
        InputStream in = null;
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(APPLICATION_PROPERTIES);
            props.load(in);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getEsClusterName() {
        return props.getProperty(esClusterName);
    }

    public static TransportAddress[] getTransportAddresses() throws UnknownHostException {
        String[] addresses = getEsTransportAddress().split(",");
        TransportAddress[] transportAddresses = new TransportAddress[addresses.length];
        for (int i = 0; i < addresses.length; i++) {
            String[] hostAndPort = addresses[i].split(":");
            transportAddresses[i] = new TransportAddress(InetAddress.getByName(hostAndPort[0]), Integer.parseInt(hostAndPort[1]));
        }
        return transportAddresses;
    }

    public static int getEsNumOfShards() {
        return Integer.parseInt(props.getProperty(esNumOfShards));
    }

    public static int getEsNumOfReplicas() {
        return Integer.parseInt(props.getProperty(esNumOfReplicas));
    }

    public static int getEsBulkActions() {
        return Integer.parseInt(props.getProperty(esBulkActions));
    }

    public static int getEsBulkSize() {
        return Integer.parseInt(props.getProperty(esBulkSize));
    }

    public static String getEsTransportAddress() {
        return props.getProperty(esTransportAddress, "localhost:9300");
    }

    public static Integer getNcitationThreshold() {
        return Integer.parseInt(props.getProperty(ncitationThreshold));
    }
}
