package fun.iweb.transport.netty;

import fun.iweb.transport.command.Header;
import fun.iweb.transport.command.ResponseHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName ResponseEncode
 * @Descrition
 * @Author
 * @Date 2020/7/19 14:30
 * @Version 1.0
 **/
public class ResponseEncode extends CommandEncode {
    @Override
    protected void encodeHeader(ChannelHandlerContext channelHandlerContext, Header header, ByteBuf byteBuf) throws Exception {
        super.encodeHeader(channelHandlerContext,header,byteBuf);
        if (header instanceof ResponseHeader) {
            ResponseHeader responseHeader = (ResponseHeader) header;
            byteBuf.writeInt(responseHeader.getCode());
            int errorLength = header.length() - 4*Integer.BYTES;
            byteBuf.writeInt(errorLength);
            byteBuf.writeBytes(responseHeader.getError() == null?new byte[0]:responseHeader.getError().getBytes(StandardCharsets.UTF_8));

        } else {
            throw new Exception(String.format("Invalid header type:%s!",header.getClass().getCanonicalName()));
        }
    }
}
