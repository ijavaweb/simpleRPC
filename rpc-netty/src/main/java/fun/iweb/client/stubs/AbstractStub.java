package fun.iweb.client.stubs;

import fun.iweb.client.RequestIdSupport;
import fun.iweb.client.ServiceStub;
import fun.iweb.client.ServiceType;
import fun.iweb.serialize.SerializerSupport;
import fun.iweb.transport.Transport;
import fun.iweb.transport.command.Code;
import fun.iweb.transport.command.Command;
import fun.iweb.transport.command.Header;
import fun.iweb.transport.command.ResponseHeader;

import java.util.concurrent.ExecutionException;

/**
 * @ClassName AbstarctStub
 * @Descrition
 * @Author
 * @Date 2020/7/20 0:04
 * @Version 1.0
 **/
public abstract class AbstractStub implements ServiceStub {
    protected Transport transport;
    protected byte[] invokeRemote(RpcRequest request) {
        Header header = new Header(ServiceType.TYPE_RPC_REQUEST,1, RequestIdSupport.next());
        byte[] payload = SerializerSupport.serialize(request);
        Command requestCommand = new Command(header,payload);
        try {
            Command responseCommand = transport.send(requestCommand).get();
            ResponseHeader responseHeader = (ResponseHeader) responseCommand.getHeader();
            if (responseHeader.getCode() == Code.SUCCESS.getCode()) {
                return responseCommand.getPayload();
            } else {
                throw new Exception(responseHeader.getError());
            }
        } catch (ExecutionException e) {
            throw  new RuntimeException(e.getCause());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setTransport(Transport transport) {
        this.transport =transport;
    }
}
