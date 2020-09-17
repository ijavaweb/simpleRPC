package fun.iweb.client.stubs;

/**
 * @ClassName RpcRequest
 * @Descrition
 * @Author
 * @Date 2020/7/19 10:59
 * @Version 1.0
 **/
public class RpcRequest {
    private final String interfaceName;
    private final String methodName;
    private final byte[] serializedArgs;

    public RpcRequest(String interfaceName, String methodName, byte[] serializedArgs) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.serializedArgs = serializedArgs;
    }
    public String getInterfaceName(){
        return interfaceName;
    }
    public String getMethodName(){
        return methodName;
    }
    public byte[] getSerializedArgs(){
        return serializedArgs;
    }
}
