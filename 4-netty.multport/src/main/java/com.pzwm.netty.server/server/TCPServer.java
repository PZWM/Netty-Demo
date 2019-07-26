package com.pzwm.netty.server.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

/**
 * @author pzwm
 * @version 1.0.0
 */
@Component
public class TCPServer {
    @Autowired
    @Qualifier("serverBootstrap")
    private ServerBootstrap b;

    @Autowired
    @Qualifier("tcpSocketAddress")
    private InetSocketAddress[] tcpPorts;

    private ChannelFuture serverChannelFuture;

    @PostConstruct
    public void start() throws Exception {
        for (InetSocketAddress tcpPort :
                tcpPorts) {

            System.out.println("Starting server at " + tcpPort);
            serverChannelFuture = b.bind(tcpPort).sync();
        }
    }

    @PreDestroy
    public void stop() throws Exception {
        serverChannelFuture.channel().closeFuture().sync();
    }

    public ServerBootstrap getB() {
        return b;
    }

    public void setB(ServerBootstrap b) {
        this.b = b;
    }

}