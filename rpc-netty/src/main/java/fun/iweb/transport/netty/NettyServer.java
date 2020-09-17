package fun.iweb.transport.netty;

import fun.iweb.transport.RequestHandlerRegistry;
import fun.iweb.transport.TransportServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName nettyServer
 * @Descrition
 * @Author
 * @Date 2020/7/19 14:48
 * @Version 1.0
 **/
public class NettyServer implements TransportServer {
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private int port;
    private EventLoopGroup acceptEvrntGroup;
    private EventLoopGroup ioEventGroup;
    private Channel channel;
    private RequestHandlerRegistry requestHandlerRegistry;

    @Override
    public void start(RequestHandlerRegistry requestHandlerRegistry, int port) throws Exception {
        this.port = port;
        this.requestHandlerRegistry = requestHandlerRegistry;
        EventLoopGroup acceptEventGroup = newEventLoopGroup();
        EventLoopGroup ioEventGroup = newEventLoopGroup();
        ChannelHandler channelHandlerPipeLine = newChannelHandlerPipeLine();
        ServerBootstrap serverBootstrap = newBootStrap(channelHandlerPipeLine,acceptEventGroup,ioEventGroup);
        Channel channel = doBind(serverBootstrap);
        this.acceptEvrntGroup = acceptEventGroup;
        this.ioEventGroup = ioEventGroup;
        this.channel = channel;
    }

    @Override
    public void stop() {
        if (acceptEvrntGroup != null) {
            acceptEvrntGroup.shutdownGracefully();
        }
        if (ioEventGroup!=null) {
            ioEventGroup.shutdownGracefully();
        }
        if (channel != null) {
            channel.close();
        }
    }

    private Channel doBind(ServerBootstrap serverBootstrap) throws Exception {
        return serverBootstrap.bind(port)
                .sync()
                .channel();
    }
    private EventLoopGroup newEventLoopGroup() {
        if (Epoll.isAvailable()) {
            return new EpollEventLoopGroup();
        } else {
            return new NioEventLoopGroup();
        }
    }

    private ChannelHandler newChannelHandlerPipeLine() {
        return new ChannelInitializer<Channel>() {

            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline()
                        .addLast(new RequestDecode())
                        .addLast(new ResponseDecode())
                        .addLast(new RequestInvocation(requestHandlerRegistry));
            }
        };
    }

    private ServerBootstrap newBootStrap(ChannelHandler channelHandler,EventLoopGroup acceptEventGroup,EventLoopGroup ioEventGroup){
     ServerBootstrap serverBootstrap = new ServerBootstrap();
     serverBootstrap.channel(Epoll.isAvailable()? EpollServerSocketChannel.class: NioServerSocketChannel.class)
             .group(acceptEventGroup,ioEventGroup)
             .childHandler(channelHandler)
             .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
     return serverBootstrap;
    }
}
