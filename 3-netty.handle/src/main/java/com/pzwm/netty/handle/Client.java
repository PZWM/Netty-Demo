package com.pzwm.netty.handle;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.sql.Timestamp;
import java.util.List;

public class Client {

    private void run() throws InterruptedException {
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(worker);
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<NioSocketChannel>() {

                @Override
                protected void initChannel(NioSocketChannel ch){
                    ch.pipeline().addLast(new TimeDecoder());
                    ch.pipeline().addLast(new ClientHandler());
                }

            });
            ChannelFuture f = b.connect("127.0.0.1", 80).sync();
            f.channel().closeFuture().sync();
        } finally {
            worker.shutdownGracefully();
        }
    }

    /**
     * 将收到当地信息转化成object加入到list中
     */
    public static final class TimeDecoder extends ByteToMessageDecoder {

        @Override
        protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
            list.add(new Timestamp(byteBuf.readLong()));
        }
    }

    public static final class ClientHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            Timestamp time = (Timestamp) msg;
            System.out.println(time.toLocalDateTime());
        }

    }

    public static void main(String[] args) {
        try {
            new Client().run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}