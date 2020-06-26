package com.zyw.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.HashMap;
import java.util.Map;

import static io.netty.handler.codec.http.HttpUtil.is100ContinueExpected;

/**
 * @author zengyw
 * @version V1.0.0
 * @description
 * @date: 22:16 2020/6/16.
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Netty server register channel handler");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Netty server unregister channel handler");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Netty server channel was active");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Netty server channel was inactive");
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 由于接收到的为Object类型数据，这边需要对该数据的实际类型做个判断并转换
        if (msg instanceof ByteBuf) {
            // 强制转换为ByteBuf类型对象
            ByteBuf byteBuf = (ByteBuf) msg;
            // 根据byteBuf中可读的字节长度建立字节数组
            byte[] bytes = new byte[byteBuf.readableBytes()];
            // 将byteBuf中的可读内容读取到bytes中（注意，这之后byteBuf就为空了）
            byteBuf.readBytes(bytes);
            // 转换为字符串
            String resp = new String(bytes);
            System.out.println("Netty server channel read the received message: " + resp);
            ctx.writeAndFlush("Netty server response => " + resp);
        } else if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;
            //100 Continue
            if (is100ContinueExpected(req)) {
                ctx.write(new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.CONTINUE));
            }
            // 获取请求的uri
            String uri = req.uri();
            Map<String, String> resMap = new HashMap<>();
            resMap.put("method", req.method().name());
            resMap.put("uri", uri);
            String content = "<html><head><title>test</title></head><body>你请求uri为：" + uri + "</body></html>";
            // 创建http响应
            FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(content, CharsetUtil.UTF_8));
            // 设置头信息
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
            //response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
            // 将html write到客户端
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            // 强制转换为String类型对象
            String req = (String) msg;
            System.out.println("Netty server channel read the received message: " + req);
            ctx.channel().writeAndFlush(Unpooled.copiedBuffer(("Netty server response => " + req).getBytes()));
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Netty server channel read complete");
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // IdleStateHandler 是实现心跳的关键, 它会根据不同的 IO idle 类型来产生不同的 IdleStateEvent 事件
        // 而这个事件的捕获, 其实就是在 userEventTriggered 方法中实现的.
        System.out.println("Netty server userEventTriggered");
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        // channel写状态变化事件
        System.out.println("Netty server channelWritabilityChanged");
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Netty server exceptionCaught: " + cause.getMessage());
        super.exceptionCaught(ctx, cause);
    }

    @Override
    protected void ensureNotSharable() {
        // 保证该handler为非共享的，该方法为非共享handler的处理逻辑
        System.out.println("Netty server ensureNotSharable");
        super.ensureNotSharable();
    }

    @Override
    public boolean isSharable() {
        // 判断这个handler是否为共享的
        System.out.println("Netty server isSharable");
        return super.isSharable();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 当前handler被加入pipeline时触发
        System.out.println("Netty server handlerAdded");
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 当前handler被移除pipeline时触发
        System.out.println("Netty server handlerRemoved");
        super.handlerRemoved(ctx);
    }
}
