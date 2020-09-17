package fun.iweb.server;

import fun.iweb.client.ServiceType;
import fun.iweb.client.stubs.RpcRequest;
import fun.iweb.serialize.SerializerSupport;
import fun.iweb.serialize.impl.Types;
import fun.iweb.transport.RequestHandler;
import fun.iweb.transport.command.Code;
import fun.iweb.transport.command.Command;
import fun.iweb.transport.command.Header;
import fun.iweb.transport.command.ResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName RpcRequestHandler
 * @Descrition
 * @Author
 * @Date 2020/7/19 12:07
 * @Version 1.0
 **/
public class RpcRequestHandler implements RequestHandler, ServiceProviderRegistry {
   private static final Logger logger = LoggerFactory.getLogger(RpcRequestHandler.class);
   private Map<String,Object> serviceProviders = new HashMap<>();

    @Override
    public synchronized <T> void addServiceProvider(Class<? extends T> serviceClass, T serviceProvide) {
        serviceProviders.put(serviceClass.getCanonicalName(),serviceProvide);
        logger.info("Add service:{},rpovider:{}",
                serviceClass.getCanonicalName(),
                serviceClass.getClass().getCanonicalName());

    }

    @Override
    public Command handle(Command requestCommand) {
        Header header = requestCommand.getHeader();
        RpcRequest rpcRequest = SerializerSupport.parse(requestCommand.getPayload());
        try {
            Object serviceProvider = serviceProviders.get(rpcRequest.getInterfaceName());
            if (serviceProvider != null) {
                String arg = SerializerSupport.parse(rpcRequest.getSerializedArgs());
                Method  method = serviceProvider.getClass().getMethod(rpcRequest.getMethodName(),String.class);
                String result = (String) method.invoke(serviceProvider,arg);
                return new Command(new ResponseHeader(type(),header.getVersion(),header.getRequestId()), SerializerSupport.serialize(result));
            }
            logger.warn("No service provider{}#{}(String)",rpcRequest.getInterfaceName(),rpcRequest.getMethodName());
            return new Command(new ResponseHeader(type(),header.getVersion(),header.getRequestId(), Code.NO_PROVIDER.getCode(),"No provider"),new byte[0]);
        } catch (Throwable throwable) {
            logger.warn("Exception",throwable);
            return new Command(new ResponseHeader(type(),header.getVersion(),header.getRequestId(),Code.UNKNOW_ERROR.getCode(),throwable.getMessage()),new byte[0]);
        }
    }

    @Override
    public int type() {
        return ServiceType.TYPE_RPC_REQUEST;
    }
}
