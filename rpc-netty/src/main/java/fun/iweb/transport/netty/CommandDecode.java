package fun.iweb.transport.netty;

import fun.iweb.transport.command.Command;
import fun.iweb.transport.command.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

/**
 * @ClassName CommandDecode
 * @Descrition
 * @Author
 * @Date 2020/7/19 13:50
 * @Version 1.0
 **/
public abstract class CommandDecode extends ByteToMessageDecoder {
    private static final int LENGTH_FIELD_LENGTH = Integer.BYTES;

    protected void encodeHeader(ChannelHandlerContext channelHandlerContext, Header header,ByteBuf byteBuf) {
        byteBuf.writeInt(header.getType());
        byteBuf.writeInt(header.getVersion());
        byteBuf.writeInt(header.getRequestId());
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (!byteBuf.isReadable(LENGTH_FIELD_LENGTH)) {
            return;
        }
        byteBuf.markReaderIndex();
        int length = byteBuf.readInt() - LENGTH_FIELD_LENGTH;

        if (byteBuf.readableBytes() < length) {
            byteBuf.resetReaderIndex();
            return ;
        }

        Header header = decodeHeader(channelHandlerContext,byteBuf);
            int payloadLength = length-header.length();

            byte[] payload = new byte[payloadLength];
            byteBuf.readBytes(payload);
            list.add(new Command(header,payload));
    }
    protected abstract Header decodeHeader(ChannelHandlerContext channelHandlerContext,ByteBuf byteBuf);

}
