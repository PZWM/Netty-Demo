package com.pzwm.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.UnsupportedEncodingException;

/**
 * 客户端发送请求
 *
 * @author pzwm
 */
public class NettyClient {

    // 要请求的服务器的ip地址
    private String ip;
    // 服务器的端口
    private int port;

    public NettyClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    // 请求端主题
    private void run() throws InterruptedException, UnsupportedEncodingException {

        EventLoopGroup bossGroup = new NioEventLoopGroup();

        Bootstrap bs = new Bootstrap();

        bs.group(bossGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        // 处理来自服务端的响应信息
                        socketChannel.pipeline().addLast(new ClientHandler());
                    }
                });

        // 客户端开启
        ChannelFuture cf = bs.connect(ip, port).sync();


        String reqStr = "这是我的第一条消息";
        reqStr = String.format("%08d", reqStr.length()) + reqStr;
        cf.channel().writeAndFlush(Unpooled.copiedBuffer(reqStr.getBytes("utf-8")));
        reqStr = "这是我的第二条消息";
        reqStr = String.format("%08d", reqStr.length()) + reqStr;
        cf.channel().writeAndFlush(Unpooled.copiedBuffer(reqStr.getBytes("utf-8")));
        reqStr = "这是我的第三条消息";
        reqStr = String.format("%08d", reqStr.length()) + reqStr;
        cf.channel().writeAndFlush(Unpooled.copiedBuffer(reqStr.getBytes("utf-8")));

        // 等待直到连接中断
        cf.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws UnsupportedEncodingException, InterruptedException {
        new NettyClient("127.0.0.1", 8090).run();
    }

}