package fun.iweb.transport.netty;

import fun.iweb.transport.InFlightRequests;
import fun.iweb.transport.ResponseFuture;
import fun.iweb.transport.Transport;
import fun.iweb.transport.command.Command;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;

import java.util.concurrent.CompletableFuture;

/**
 * @ClassName NettyTransport
 * @Descrition
 * @Author
 * @Date 2020/7/19 16:23
 * @Version 1.0
 **/
public class NettyTransport implements Transport {
    private final Channel channel;
    private final InFlightRequests inFlightRequests;

    NettyTransport(Channel channel,InFlightRequests inFlightRequests) {
        this.channel = channel;
        this.inFlightRequests = inFlightRequests;
    }
    @Override
    public CompletableFuture<Command> send(Command request) {
        CompletableFuture<Command> completableFuture = new CompletableFuture<>();
        try {
            inFlightRequests.put(new ResponseFuture(request.getHeader().getRequestId(),completableFuture));
            channel.writeAndFlush(request).addListener((ChannelFutureListener) channelFuture->{
                if (channelFuture.isSuccess()) {
                    completableFuture.completeExceptionally(channelFuture.cause());
                    channel.close();
                }
            });
        } catch (Throwable throwable){
            inFlightRequests.remove(request.getHeader().getRequestId());
            completableFuture.completeExceptionally(throwable);
        }

        return completableFuture;

    }
}
