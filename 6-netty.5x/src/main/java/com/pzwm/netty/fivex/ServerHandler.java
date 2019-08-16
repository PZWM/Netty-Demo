package com.pzwm.netty.fivex;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg);
        System.out.println("一次执行");
        System.out.println("length="+msg.length());
        ctx.writeAndFlush("给你个回话");

    }
}