package fun.iweb.transport.netty;

import fun.iweb.transport.command.Header;
import fun.iweb.transport.command.ResponseHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName ResponseDecode
 * @Descrition
 * @Author
 * @Date 2020/7/19 14:30
 * @Version 1.0
 **/
public class ResponseDecode extends CommandDecode {
    @Override
    protected Header decodeHeader(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        int type = byteBuf.readInt();
        int version = byteBuf.readInt();
        int requestId = byteBuf.readInt();
        int code = byteBuf.readInt();
        int errorLength = byteBuf.readInt();
        byte[] errorBytes = new byte[errorLength];
        byteBuf.readBytes(errorBytes);
        String error = new String(errorBytes, StandardCharsets.UTF_8);
        return new ResponseHeader(type,version,requestId,code,error);
    }
}
