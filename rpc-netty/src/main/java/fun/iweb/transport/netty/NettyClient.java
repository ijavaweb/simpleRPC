package fun.iweb.transport.netty;

import fun.iweb.transport.InFlightRequests;
import fun.iweb.transport.Transport;
import fun.iweb.transport.TransportClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName nettyClient
 * @Descrition
 * @Author
 * @Date 2020/7/19 14:48
 * @Version 1.0
 **/
public class NettyClient implements TransportClient {
    private EventLoopGroup ioEventGroup;
    private Bootstrap bootstrap;
    private final InFlightRequests inFlightRequests;
    private List<Channel> channelList = new LinkedList<>();

    public  NettyClient() {
        inFlightRequests = new InFlightRequests();
    }

    private Bootstrap newBootStrap(ChannelHandler channelHandler,EventLoopGroup ioEventGroup) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(Epoll.isAvailable()? EpollSocketChannel.class: NioSocketChannel.class)
                .group(ioEventGroup)
                .handler(channelHandler)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        return bootstrap;
    }
    @Override
    public Transport createTransport(SocketAddress address, long connectionTimeout) throws InterruptedException, TimeoutException {
        return new NettyTransport(createChannel(address,connectionTimeout),inFlightRequests);
    }

    private ChannelHandler newChannelHandlerPipeLine() {
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) {
                channel.pipeline()
                        .addLast(new ResponseDecode())
                        .addLast(new RequestEncode())
                        .addLast(new ResponseInvocation(inFlightRequests));
            }
        };
    }

    private EventLoopGroup newIoEventGroup() {
        if (Epoll.isAvailable()) {
            return new EpollEventLoopGroup();
        } else {
            return new NioEventLoopGroup();
        }
    }




    private synchronized Channel createChannel(SocketAddress address,long connectionTimeOut) throws InterruptedException,TimeoutException {
        if (address == null) {
            throw new IllegalArgumentException("address must not be empty");
        }

        if (ioEventGroup == null) {
            ioEventGroup = newIoEventGroup();
        }
        if (bootstrap == null) {
            ChannelHandler channelHandler = newChannelHandlerPipeLine();
            bootstrap = newBootStrap(channelHandler,ioEventGroup);
        }
        ChannelFuture channelFuture;
        Channel channel;
        channelFuture = bootstrap.connect(address);
        if (!channelFuture.await(connectionTimeOut)) {
            throw new TimeoutException();
        }

        channel = channelFuture.channel();

        if (channel == null || channel.isActive()) {
            throw new IllegalStateException();
        }
        channelList.add(channel);
        return channel;
    }
    @Override
    public void close() {
        for (Channel channel : channelList) {
            if (null != channel) {
                channel.close();
            }
        }

        if (ioEventGroup != null) {
            ioEventGroup.shutdownGracefully();
        }
        try {
            inFlightRequests.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
