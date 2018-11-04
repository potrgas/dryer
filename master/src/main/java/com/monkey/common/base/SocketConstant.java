package com.monkey.common.base;

/**
 * @author liugh
 * @since 2018-05-03
 */
public enum SocketConstant {

    /**
     * 异常
     */
    HEART(0, "心跳"),
    BUSINESS(1,"业务"),
    CHANGE(3,"交流"),
    COMMOND(4,"指令"),

    ;


    public Integer type;
    public String content;

    SocketConstant(Integer type, String content) {
        this.type = type;
        this.content = content;
    }

}
