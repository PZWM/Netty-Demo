package com.pzwm.netty.server.handles;

import com.pzwm.netty.utils.MyStringUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * @author pzwm
 * @version 1.0.0
 */
@Component
@Qualifier("serverHandler")
@ChannelHandler.Sharable
@Configurable
public class ServerHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);


    @Value("${netty.socket.response.length}")
    private int RESPONSE_LENGTH;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg)
            throws Exception {
        log.info("client msg:" + msg);
        String clientIdToLong = ctx.channel().id().asLongText();
        log.info("client long id:" + clientIdToLong);
        String clientIdToShort = ctx.channel().id().asShortText();
        log.info("client short id:" + clientIdToShort);
        if (msg.indexOf("bye") != -1) {
            //close
            ctx.channel().close();
        } else {
            //send to client
            int length = -1;
            try {
                //长度验证
                length = Integer.parseInt(msg.substring(0, RESPONSE_LENGTH));
            } catch (NumberFormatException e) {
                msg = "00000000报文格式不正确";
                log.error(e.getMessage(), e);
            }
            if (length != (msg.length() - RESPONSE_LENGTH)) {
                // 将表示报文长度的字段截掉
                msg = msg.substring(RESPONSE_LENGTH);
                msg="error msg is:" + msg;
                msg = String.format("%08d", msg.length()) + msg;
                ctx.channel().writeAndFlush(msg);
                return;
            }

           msg= "Your msg is:" + msg;
            msg = String.format("%08d", msg.length()) + msg;
            ctx.channel().writeAndFlush(msg);
            log.info("return msg:" + msg);

        }

    }

    /**
     * 这里是新连接接入时，会由服务发出的消息
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        log.info("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");

        String msg="Welcome to " + InetAddress.getLocalHost().getHostName() + " service!\n";

        msg=MyStringUtil.addLenth(msg);

        ctx.channel().writeAndFlush(msg);

        super.channelActive(ctx);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("\nChannel is disconnected");
        super.channelInactive(ctx);
    }


}
