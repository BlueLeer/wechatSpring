package com.lee.wechat.controller;

import com.google.gson.Gson;
import com.lee.wechat.entity.Constant;
import com.lee.wechat.entity.TokenInfo;
import com.lee.wechat.entity.UserInfo;
import com.lee.wechat.util.NetWorkHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 微信授权相关
 */
@Controller
@RequestMapping("/wechat")
public class WechatController {

    @RequestMapping("/wechatLogin.action")
    public String wechatLogin(HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute("userinfo");
        if (userInfo == null) {
            return "/wechat/authorization.action";
        } else {
            return "/jsp/userinfo.jsp";
        }
    }

    /**
     * 引导用户进入微信授权页面,记得需要在微信开发平台修改网页授权页面域名
     * 微信只弹出一次授权,第二次的时候就直接跳转到 redirect_uri 页面去了
     *
     * @return
     */
    @RequestMapping("/authorization.action")
    public String authorization() {
        String URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=STATE#wechat_redirect";
        String APPID = "wx24cdc78e365daee0";
        String REDIRECT_URI = Constant.BASE_URL + "/wechat/getAccessToken.action"; // 回调地址

        String SCOPE = "snsapi_userinfo"; // 采用网页授权的方式
        // 引导用户进入微信用户授权的页面的url
        String url = String.format(URL, APPID, REDIRECT_URI, SCOPE);

        return "redirect:" + url;
    }


    /**
     * 用户点击页面上的"确认登录"以后,回调的地址
     *
     * @param code  授权码,有了授权码以后才能获取用户的openID
     * @param state
     * @return
     */
    @RequestMapping("/getAccessToken.action")
    public String getAccessToken(String code, String state, HttpSession session) throws IOException {
        String APPID = "wx24cdc78e365daee0";
        String APPSECRECT = "37b4c3c4c4d381175bf7b27a2653a5fc";
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
        // 将appID、appsecrect、以及code传入上面的url用于获取ACCESS_TOKEN
        String accessURL = String.format(url, APPID, APPSECRECT, code);

        NetWorkHelper netWorkHelper = new NetWorkHelper();
        // 通过get方式获取结果集
        /*
        弱请求正确,返回的结果集如下:
        {
            "access_token":"ACCESS_TOKEN",
            "expires_in":7200,
            "refresh_token":"REFRESH_TOKEN",
            "openid":"OPENID",
            "scope":"SCOPE"
        }
         */

        String result = netWorkHelper.getHttpsResponse(accessURL, "GET");

        Gson gson = new Gson();
        TokenInfo tokenInfo = gson.fromJson(result, TokenInfo.class);

        // 通过获取到的access_token和openid获取用户的信息
        UserInfo userinfo = getUserinfo(tokenInfo);
        // 获取到ACCESS_TOKEN和openid以后,将它们存进session中去
        session.setAttribute("userinfo", userinfo);

        // 将用户的信息存进数据库


        return "/jsp/userinfo.jsp";
    }

//    @RequestMapping("/printUserInfo.action")
//    public String printUserInfo(ModelAndView mv) {
//        String ACCESS_TOKEN = "12_aIonWRR7wTcvuNsUIQJaXQxqRv68hJYckwiRszWo70COQcRJDQ3bO5hnmrM2x_EqyzhjSLFtp4kT-o_PnopdHw";
//        String openID = "oYHqI0-jg-vRhksQAMHceUB75ZsA";
//
//        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
//        String realUrl = String.format(url, ACCESS_TOKEN, openID);
//        NetWorkHelper helper = new NetWorkHelper();
//        String userinfoJson = helper.getHttpsResponse(realUrl, "GET");
//        System.out.println(userinfoJson);
//
//        return "/jsp/userinfo.jsp";
//
//    }


    /**
     * 通过Token中的ACCESS_TOKEN和openID获取用户信息
     */
    private UserInfo getUserinfo(TokenInfo tokenInfo) throws UnsupportedEncodingException {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
        String realUrl = String.format(url, tokenInfo.getAccess_token(), tokenInfo.getOpenid());
        NetWorkHelper netWorkHelper = new NetWorkHelper();
        String jsonResult = netWorkHelper.getHttpsResponse(realUrl, "GET");

        // 微信采用ISO-8859-1编码,需要对获取的结果进行编码
        String utfResult = new String(jsonResult.getBytes("ISO-8859-1"), "UTF-8");
        System.out.println("utfResult-------------" + utfResult);


        Gson gson = new Gson();
        UserInfo userInfo = gson.fromJson(utfResult, UserInfo.class);

        // 需要转一下码
        userInfo.setNickname(new String(userInfo.getNickname().getBytes("ISO-8859-1"), "UTF-8"));
        System.out.println(userInfo);
        return userInfo;
    }


}
