package fun.iweb.transport.netty;

import fun.iweb.transport.command.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName RequestDecode
 * @Descrition
 * @Author
 * @Date 2020/7/19 14:07
 * @Version 1.0
 **/
public class RequestDecode extends CommandDecode {
    @Override
    protected Header decodeHeader(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        return new Header(
                byteBuf.readInt(),
                byteBuf.readInt(),
                byteBuf.readInt()
        );
    }
}
