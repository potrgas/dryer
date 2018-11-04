package com.monkey.web.controller.dtos;

import com.monkey.common.base.SocketConstant;

public class WebSocketMessage {
    public WebSocketMessage() {
    }

    public WebSocketMessage(String to, String data, SocketConstant type) {
        this.to = to;
        this.type = type;
        this.data = data;
    }

    public String to;
    public String data;
    public SocketConstant type;
}
