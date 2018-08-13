package com.tml.netty;

import com.tml.netty.config.NettyConfig;
import com.tml.netty.handler.ChannelHandler;
import com.tml.netty.handler.MessageDecoder;
import com.tml.netty.handler.MessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NettyApplication {

    private static EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private static EventLoopGroup workerGroup = new NioEventLoopGroup();
    @Autowired
    ChannelHandler channelHandler;
    @Autowired
    MessageDecoder messageDecoder;
    @Autowired
    MessageEncoder messageEncoder;
    @Bean("nettyConfig")
    @ConfigurationProperties(prefix = "netty.config")
    public NettyConfig config() {
        return new NettyConfig();
    }

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext run = SpringApplication.run(NettyApplication.class, args);
        ServerBootstrap serverBootstrap = (ServerBootstrap) run.getBean("serverBootstrap");
        NettyConfig config = (NettyConfig) run.getBean("nettyConfig");
        try {
            ChannelFuture sync = serverBootstrap.bind(config.getPort()).sync();
            sync.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Bean("serverBootstrap")
    public ServerBootstrap serverBootstrap() {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new MessageDecoder());
                        socketChannel.pipeline().addLast(new MessageEncoder());
                        socketChannel.pipeline().addLast(channelHandler);
                    }
                })
                .childOption(ChannelOption.SO_KEEPALIVE,true);
        return b;
    }
}
