package com.zyw.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author zengyw
 * @version V1.0.0
 * @description
 * @date: 21:41 2020/6/16.
 */
public class NettyServer extends Thread{
    // linux环境下  使用epoll事件通知机制
//    private EventLoopGroup boss = new EpollEventLoopGroup();
//    private EventLoopGroup worker = new EpollEventLoopGroup();

    // 非linux环境下  无法使用epoll事件通知机制
    private EventLoopGroup boss = new NioEventLoopGroup();
    private EventLoopGroup worker = new NioEventLoopGroup();
    private ServerBootstrap serverBootstrap = new ServerBootstrap();
    private ChannelFuture channelFuture = null;

    private Integer port;

    public NettyServer (int port) {
        this.port = port;
    }

    @Override
    public void run() {
        startServer();
    }

    private void startServer() {
        try {
            // 初始化服务配置
            serverBootstrap.group(boss,worker)
                // 设置子通道最大发送缓存
                .childOption(ChannelOption.SO_SNDBUF, 1024*16)
                // 设置子通道最大接收缓存
                .childOption(ChannelOption.SO_RCVBUF,1024*16)
                // BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度
                .childOption(ChannelOption.SO_BACKLOG,1024)
                .channel(NioServerSocketChannel.class)
                .childHandler(new NettyServerInitializer());
            // 绑定监听端口，同步等待连接成功
            channelFuture = serverBootstrap.bind(port).sync();
            // 等待服务端监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            stopServer();
        }
    }

    private void stopServer() {
        // 优雅退出
        boss.shutdownGracefully();
        worker.shutdownGracefully();
    }
}
