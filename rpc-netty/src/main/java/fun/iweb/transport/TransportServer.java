package fun.iweb.transport;

public interface TransportServer {
    void start(RequestHandlerRegistry requestHandlerRegistry,int port) throws Exception;
    void stop();
}
