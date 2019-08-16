package com.pzwm.netty.core.service;

import com.pzwm.netty.core.dto.NettyMessage;

import java.util.Map;

public interface MessageProcess {

    NettyMessage parseMessageToMap(String msg);
}
