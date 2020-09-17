package fun.iweb;

import fun.iweb.client.StubFactory;
import fun.iweb.serialize.SerializerSupport;
import fun.iweb.server.ServiceProviderRegistry;
import fun.iweb.spi.ServiceSupport;
import fun.iweb.transport.RequestHandlerRegistry;
import fun.iweb.transport.Transport;
import fun.iweb.transport.TransportClient;
import fun.iweb.transport.TransportServer;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.Provider;
import java.sql.Time;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName NettyRpcAccessPoint
 * @Descrition
 * @Author
 * @Date 2020/7/20 0:17
 * @Version 1.0
 **/
public class NettyRpcAccessPoint implements RpcAccessPoint{
    private final String host = "localhost";
    private final int port = 9999;
    private  final URI uri = URI.create("rpc://" +host+":"+port);
    private TransportServer server = null;
    private TransportClient client = ServiceSupport.load(TransportClient.class);
    private final Map<URI, Transport> clientMap = new ConcurrentHashMap<>();
    private final StubFactory stubFactory = ServiceSupport.load(StubFactory.class);
    private final ServiceProviderRegistry serviceProviderRegistry = ServiceSupport.load(ServiceProviderRegistry.class);
    @Override
    public <T> T getRemoteService(URI uri, Class<T> serviceClass) {
        Transport transport = clientMap.computeIfAbsent(uri,this::createTransport);
        return stubFactory.createStub(transport,serviceClass);
    }
    private Transport createTransport(URI uri) {
        try {
            return client.createTransport(new InetSocketAddress(uri.getHost(),uri.getPort()),30000L);
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> URI addServiceProvider(T service, Class<T> serviceClass) {
        serviceProviderRegistry.addServiceProvider(serviceClass,service);
        return uri;
    }

    @Override
    public synchronized Closeable startServer() throws Exception {
        if (null == server) {
            server = ServiceSupport.load(TransportServer.class);
            server.start(RequestHandlerRegistry.getInstance(),port);
        }
        return ()->{
            if (null!=server) {
                server.stop();
            }
        };
    }

    @Override
    public void close() throws IOException {
        if (null!=server) {
            server.stop();
        }
        client.close();
    }
}
