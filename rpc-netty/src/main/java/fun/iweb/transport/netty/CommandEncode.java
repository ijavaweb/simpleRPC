package fun.iweb.transport.netty;

import fun.iweb.transport.command.Command;
import fun.iweb.transport.command.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @ClassName CommandEncode
 * @Descrition
 * @Author
 * @Date 2020/7/19 14:12
 * @Version 1.0
 **/
public abstract class CommandEncode extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        if(!(o instanceof Command)) {
            throw new Exception(String.format("Unknown type: %s!", o.getClass().getCanonicalName()));
        }

        Command command = (Command) o;
        byteBuf.writeInt(Integer.BYTES + command.getHeader().length() + command.getPayload().length);
        encodeHeader(channelHandlerContext, command.getHeader(), byteBuf);
        byteBuf.writeBytes(command.getPayload());


    }

    protected void encodeHeader(ChannelHandlerContext channelHandlerContext, Header header, ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(header.getType());
        byteBuf.writeInt(header.getVersion());
        byteBuf.writeInt(header.getRequestId());
    }
}
