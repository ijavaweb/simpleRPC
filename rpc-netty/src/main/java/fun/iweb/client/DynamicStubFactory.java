package fun.iweb.client;

import com.itranswarp.compiler.JavaStringCompiler;
import fun.iweb.transport.Transport;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @ClassName DynamicStubFactory
 * @Descrition
 * @Author
 * @Date 2020/7/19 17:59
 * @Version 1.0
 **/
public class DynamicStubFactory implements StubFactory{
    private final static String STUB_SOURCE_TEMPLATE =
            "package fun.iweb.client.stubs;\n" +
                    "import fun.iwebc.serialize.SerializeSupport;\n" +
                    "\n" +
                    "public class %s extends AbstractStub implements %s {\n" +
                    "    @Override\n" +
                    "    public String %s(String arg) {\n" +
                    "        return SerializeSupport.parse(\n" +
                    "                invokeRemote(\n" +
                    "                        new RpcRequest(\n" +
                    "                                \"%s\",\n" +
                    "                                \"%s\",\n" +
                    "                                SerializeSupport.serialize(arg)\n" +
                    "                        )\n" +
                    "                )\n" +
                    "        );\n" +
                    "    }\n" +
                    "}";
    @Override
    @SuppressWarnings("unchecked")
    public <T> T createStub(Transport transport, Class<T> serviceClass) {
        try {
            String stubSimpleName = serviceClass.getSimpleName()+"Stub";
            String classFullName = serviceClass.getName();
            String stubFullName = "fun.iweb.client.stubs" + stubSimpleName;
            String methodName = serviceClass.getMethods()[0].getName();

            String source = String.format(STUB_SOURCE_TEMPLATE,stubSimpleName,classFullName,methodName,classFullName,methodName);

            JavaStringCompiler compiler = new JavaStringCompiler();
            Map<String,byte[]> results = compiler.compile(stubSimpleName+".java",source);

            Class<?> clazz = compiler.loadClass(stubFullName,results);

            ServiceStub stubInstance = (ServiceStub) clazz.newInstance();
            stubInstance.setTransport(transport);
            return (T) stubInstance;
        } catch (Throwable throwable) {
            throw  new RuntimeException(throwable);
        }
    }
}
