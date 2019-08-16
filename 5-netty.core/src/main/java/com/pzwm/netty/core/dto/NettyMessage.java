package com.pzwm.netty.core.dto;

import com.pzwm.netty.core.constant.MessageType;
import lombok.Data;

@Data
public class NettyMessage {

    private MessageType messageType;

}
