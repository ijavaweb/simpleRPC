package fun.iweb.transport.netty;

import fun.iweb.transport.command.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName RequestEncode
 * @Descrition
 * @Author
 * @Date 2020/7/19 14:06
 * @Version 1.0
 **/
public class RequestEncode extends CommandEncode{
    @Override
    protected void encodeHeader(ChannelHandlerContext channelHandlerContext, Header header, ByteBuf byteBuf) throws Exception {
        super.encodeHeader(channelHandlerContext,header,byteBuf);
    }
}
