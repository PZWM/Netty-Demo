package com.pzwm.netty.server.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.List;

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
    @Qualifier("OtherServerBootstrap")
    private ServerBootstrap a;

    @Autowired
    @Qualifier("tcpSocketAddress")
    private InetSocketAddress[] tcpPorts;
    @Autowired
    @Qualifier("otherTcpSocketAddress")
    private InetSocketAddress[] otherPorts;

    private List<ChannelFuture> serverChannelFutures;

    @PostConstruct
    public void start() throws Exception {
        for (InetSocketAddress tcpPort :
                tcpPorts) {

            System.out.println("Starting server1 at " + tcpPort);
            serverChannelFutures.add(b.bind(tcpPort).sync());
        }
        for (InetSocketAddress tcpPort :
                otherPorts) {

            System.out.println("Starting server2 at " + tcpPort);
            serverChannelFutures.add(a.bind(tcpPort).sync());
        }
    }

    @PreDestroy
    public void stop() throws Exception {
        for (ChannelFuture serverChannelFuture : serverChannelFutures)
            serverChannelFuture.channel().close().sync();
    }

    public ServerBootstrap getB() {
        return b;
    }

    public void setB(ServerBootstrap b) {
        this.b = b;
    }

}