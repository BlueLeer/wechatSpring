package com.lee.wechat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Controller
@RequestMapping("/wechat")
public class JoinWechatController {

    private static final String TOKEN = "wangle26598645641654";

    /** 微信接入,必须要返回一个纯的echostr字符串才可以
     * @param signature 微信加密签名signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     * @return 如果校验成功, 返回包含echostr字符串的信息给微信
     */
    @RequestMapping("/join.action")
    public void join(String signature, String timestamp, String nonce, String echostr, HttpServletResponse response) throws IOException {
        System.out.println(signature);
        System.out.println(timestamp);
        System.out.println(nonce);
        System.out.println(echostr);
        //排序,并将参数拼接成一个字符串
        String sortString = sort(TOKEN, timestamp, nonce);
        //加密
        String mySignature = sha1(sortString);


        //校验签名
        if (mySignature != null && mySignature != "" && mySignature.equals(signature)) {
            response.getWriter().write(echostr);
        } else {
            response.getWriter().write("error");
        }
    }


    /**
     * 排序方法
     *
     * @param token
     * @param timestamp
     * @param nonce
     * @return
     */
    private String sort(String token, String timestamp, String nonce) {
        String[] strArray = {token, timestamp, nonce};
        Arrays.sort(strArray);
        StringBuilder sb = new StringBuilder();
        for (String str : strArray) {
            sb.append(str);
        }

        return sb.toString();
    }

    /**
     * 将字符串进行sha1加密
     *
     * @param str 需要加密的字符串
     * @return 加密后的内容
     */
    private String sha1(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
