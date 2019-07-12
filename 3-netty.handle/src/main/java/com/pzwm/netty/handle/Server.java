package com.pzwm.netty.handle;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.MessageToByteEncoder;

import java.sql.Timestamp;
import java.util.Date;

public class Server {


    private void run() throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, worker);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new ChannelInitializer<NioSocketChannel>() {

                @Override
                protected void initChannel(NioSocketChannel ch) {
                    ch.pipeline().addLast(new TimeServerHandler());
                    ch.pipeline().addFirst(new TimeEncoder());
                }

            });
            ChannelFuture f = b.bind(80).sync();
            f.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    /**
     * 这里继承MessageToByteEncoder，将message的类型通过泛型传入继承的类
     * 通过ByteBuf的writeLong方法将已经转化为基础类型的数据传出
     */
    private static final class TimeEncoder extends MessageToByteEncoder<Timestamp> {

        @Override
        protected void encode(ChannelHandlerContext ctx, Timestamp msg,
                              ByteBuf out)  {
            out.writeLong(msg.getTime());
        }

    }

    /**
     * 向连接到该服务的客户端发送4次时间消息
     */
    private static final class TimeServerHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            ctx.pipeline().write(new Timestamp(new Date().getTime()));
            ctx.pipeline().write(new Timestamp(new Date().getTime()));
            ctx.pipeline().write(new Timestamp(new Date().getTime()));
            ctx.pipeline().write(new Timestamp(new Date().getTime())).channel().pipeline().flush();
            //发送完毕，关闭频道
            ctx.channel().close();
        }


    }

    public static void main(String[] args) {
        try {
            new Server().run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}