# Netty从零开始

首先需要启动server

### 1-netty.server
启动服务后。可以使用telnet工具进行服务测试。
```
  telnet 127.0.0.1 8090
```

业务处理在ServerHandler中的channelRead0方法中写处理逻辑

### 2-netty.client
启动该服务可以向启动的netty server发送文本

### 3-netty.handle
该模块中主要针对定义encoder和decoder做了演示。


### 4-netty.multport
该模块中主要针对netty绑定多个端口做展示。同时，也针对启动不同的netty服务做了展示。

在该模块中存在两个不同的server启动，分别绑定了7001-7003端口及7004、7005端口。
```
tcp:
  ports:
    - 7001
    - 7002
    - 7003
  others:
    - 7004
    - 7005
```
针对两个不同的（该项目中并没有什么不同的处理，如果需要在NettyConfig中OtherServerBootstrap去改变处理逻辑）服务分别作自己逻辑处理。


