package com.zyw.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author zengyw
 * @version V1.0.0
 * @description
 * @date: 21:55 2020/6/16.
 */
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 分隔符解码器
//        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast(new StringDecoder());
        // 编码器
        pipeline.addLast(new ByteArrayEncoder());
        // http编解码器
//        pipeline.addLast(new HttpServerCodec());
        // http消息聚合器
//        pipeline.addLast(new HttpObjectAggregator(512*1024));
        // 请求处理器
        pipeline.addLast(new NettyServerHandler());
    }
}
