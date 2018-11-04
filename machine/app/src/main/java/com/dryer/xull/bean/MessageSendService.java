package com.dryer.xull.bean;

public class MessageSendService {

    /**
     * message : 链接服务器成功
     * order :
     * state : true
     * to : aaaa
     * type : 1
     */

    private String message;
    private String order;
    private boolean state;
    private String to="aaaa";
    private int type=1;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
