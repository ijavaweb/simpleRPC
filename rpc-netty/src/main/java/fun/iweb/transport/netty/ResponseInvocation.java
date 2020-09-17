package fun.iweb.transport.netty;

import fun.iweb.transport.InFlightRequests;
import fun.iweb.transport.ResponseFuture;
import fun.iweb.transport.command.Command;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName ResponseInvocation
 * @Descrition
 * @Author
 * @Date 2020/7/19 14:30
 * @Version 1.0
 **/
public class ResponseInvocation extends SimpleChannelInboundHandler<Command> {
    private static final Logger logger = LoggerFactory.getLogger(ResponseInvocation.class);
    private  final InFlightRequests inFlightRequests;

    ResponseInvocation (InFlightRequests inFlightRequests) {
        this.inFlightRequests = inFlightRequests;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Command response) throws Exception {
        ResponseFuture future = inFlightRequests.remove(response.getHeader().getRequestId());
        if (null != future) {
            future.getFuture().complete(response);
        } else {
            logger.warn("Drop response:{}", response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext,Throwable cause) throws Exception {
        logger.warn("Exception:", cause);
        super.exceptionCaught(channelHandlerContext,cause);
        Channel channel = channelHandlerContext.channel();
        if (channel.isActive()) {
            channelHandlerContext.close();
        }

    }
}
