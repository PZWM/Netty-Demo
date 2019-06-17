package com.pzwm.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;

/**
 * 读取服务器返回的响应信息
 *
 * @author pzwm
 *
 */
@Configurable
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);

    private int RESPONSE_LENGTH=8;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        try {
            ByteBuf bb = (ByteBuf)msg;
            byte[] respByte = new byte[bb.readableBytes()];
            bb.readBytes(respByte);
            String respStr = new String(respByte, "utf-8");

            int length = -1;
            try {
                //长度验证
                length = Integer.parseInt(respStr.substring(0, RESPONSE_LENGTH));
            } catch (NumberFormatException e) {
                msg = "报文格式不正确";
                log.error(e.getMessage(), e);
            }
            if (length != (respStr.length() - RESPONSE_LENGTH)) {
                // 将表示报文长度的字段截掉
                respStr = respStr.substring(RESPONSE_LENGTH);
                ctx.channel().writeAndFlush("error msg is:" + msg);
                return;
            }
            System.out.println("client--收到响应：" + respStr);

            // 直接转成对象
//          handlerObject(ctx, msg);

        } finally{
            // 必须释放msg数据
            ReferenceCountUtil.release(msg);

        }

    }
//
//    private void handlerObject(ChannelHandlerContext ctx, Object msg) {
//
//        Student student = (Student)msg;
//        System.err.println("server 获取信息："+student.getId()+student.getName());
//    }


    // 数据读取完毕的处理
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端读取数据完毕");
    }

    // 出现异常的处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("client 读取数据出现异常");
        ctx.close();
    }

}
