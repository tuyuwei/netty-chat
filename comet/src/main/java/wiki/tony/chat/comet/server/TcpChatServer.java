package wiki.tony.chat.comet.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import wiki.tony.chat.comet.ChatServer;
import wiki.tony.chat.comet.initializer.TcpServerInitializer;

/**
 * tcp server
 * <p>
 * Created by Tony on 4/13/16.
 */
@Component("tcpChatServer")
public class TcpChatServer implements ChatServer {

    private Logger logger = LoggerFactory.getLogger(TcpChatServer.class);

    @Value("${server.tcp.port:9090}")
    private int port;
    @Autowired
    private TcpServerInitializer serverInitializer;

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workGroup = new NioEventLoopGroup();

    private ChannelFuture channelFuture;

    @Override
    public void start() throws Exception {
        try {
            ServerBootstrap b = new ServerBootstrap()
                    .group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(serverInitializer);

            logger.info("Starting TcpChatServer... Port: " + port);

            channelFuture = b.bind(port).sync();

        } finally {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    shutdown();
                }
            });
        }
    }

    @Override
    public void restart() throws Exception {
        shutdown();
        start();
    }

    @Override
    public void shutdown() {
        if (channelFuture != null) {
            channelFuture.channel().closeFuture();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workGroup != null) {
            workGroup.shutdownGracefully();
        }
    }

}
