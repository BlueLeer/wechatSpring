package com.lee.wechat.entity;

public class Echostr {
    private String echostr;

    public String getEchostr() {
        return echostr;
    }

    public void setEchostr(String echostr) {
        this.echostr = echostr;
    }

    @Override
    public String toString() {
        return "Echostr{" +
                "echostr='" + echostr + '\'' +
                '}';
    }
}
