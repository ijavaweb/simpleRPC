package fun.iweb.transport.netty;

import fun.iweb.transport.RequestHandler;
import fun.iweb.transport.RequestHandlerRegistry;
import fun.iweb.transport.command.Command;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName RequestInvocation
 * @Descrition
 * @Author
 * @Date 2020/7/19 14:07
 * @Version 1.0
 **/
@ChannelHandler.Sharable
public class RequestInvocation extends SimpleChannelInboundHandler<Command> {
    private static final Logger logger = LoggerFactory.getLogger(RequestInvocation.class);
    private final RequestHandlerRegistry requestHandlerRegistry;
    RequestInvocation(RequestHandlerRegistry requestHandlerRegistry) {
        this.requestHandlerRegistry = requestHandlerRegistry;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Command command) throws Exception {
        RequestHandler handler = requestHandlerRegistry.get(command.getHeader().getType());
        if (null!=handler) {
            Command response = handler.handle(command);
            if (null != response) {
                channelHandlerContext.writeAndFlush(response).addListener((ChannelFutureListener) channelFuture->{
                    if (!channelFuture.isSuccess()) {
                        logger.warn("Write response failed",channelFuture.cause());
                        channelHandlerContext.channel().close();
                    }
                });
            } else {
                logger.warn("Response is null");
            }
        } else {
            throw new Exception(String.format("No handler for request with type:%s!",command.getHeader().getType()));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext,Throwable throwable) throws Exception {
        logger.warn("Exception",throwable);
        super.exceptionCaught(channelHandlerContext,throwable);
        Channel channel = channelHandlerContext.channel();
        if (channel.isActive()) {
            channelHandlerContext.close();
        }
    }
}
