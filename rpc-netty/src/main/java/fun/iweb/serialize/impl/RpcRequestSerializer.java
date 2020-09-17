package fun.iweb.serialize.impl;

import fun.iweb.client.stubs.RpcRequest;
import fun.iweb.serialize.Serializer;
import io.netty.buffer.ByteBufUtil;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName RpcRequestSerializer
 * @Descrition
 * @Author
 * @Date 2020/7/19 10:57
 * @Version 1.0
 **/
public class RpcRequestSerializer implements Serializer<RpcRequest> {
    @Override
    public int size(RpcRequest entry) {
        return Integer.BYTES + entry.getInterfaceName().getBytes(StandardCharsets.UTF_8).length+
                Integer.BYTES+entry.getMethodName().getBytes(StandardCharsets.UTF_8).length+
                Integer.BYTES+entry.getSerializedArgs().length;
    }

    @Override
    public void serialize(RpcRequest entry, byte[] bytes, int offset, int length) {

        ByteBuffer buffer = ByteBuffer.wrap(bytes,offset,length);
        byte[] tmpBytes = entry.getInterfaceName().getBytes(StandardCharsets.UTF_8);
        buffer.putInt(tmpBytes.length);
        buffer.put(tmpBytes);

        tmpBytes = entry.getMethodName().getBytes(StandardCharsets.UTF_8);
        buffer.putInt(tmpBytes.length);
        buffer.put(tmpBytes);

        tmpBytes = entry.getSerializedArgs();
        buffer.putInt(tmpBytes.length);
        buffer.put(tmpBytes);
    }

    @Override
    public RpcRequest parse(byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes,offset,length);
        int len = buffer.getInt();
        byte[] temBytes = new byte[len];
        buffer.get(temBytes);
        String interfaceName = new String(temBytes,StandardCharsets.UTF_8);

        len = buffer.getInt();
        temBytes = new byte[len];
        buffer.get(temBytes);
        String methodName = new String(temBytes,StandardCharsets.UTF_8);

        len = buffer.getInt();
        temBytes = new byte[len];
        buffer.get(temBytes);
        byte[] serializerArgs = temBytes;
        return new RpcRequest(interfaceName,methodName,serializerArgs);
    }

    @Override
    public byte type() {
        return Types.TYPE_RPC_REQUEST;
    }

    @Override
    public Class<RpcRequest> getSerialiazeClass() {
        return RpcRequest.class;
    }
}
