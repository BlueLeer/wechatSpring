package com.lee.wechat.entity;

/**
 * 基本用户信息
 */
public class UserInfo {

    private String openid;
    private String nickname;
    private String sex; // 性别 1代表男性,2代表女性
    private String province; // 省份
    private String city; // 城市
    private String county; // 国家
    private String headimgurl; // 头像
    private String[] privilege; // 用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
    private String unionid; // 只有将公众号绑定到微信开放平台帐号后，才会出现该字段

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String[] getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String[] privilege) {
        this.privilege = privilege;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "openid='" + openid + '\'' +
                ", nikename='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", privilege='" + privilege + '\'' +
                ", unionid='" + unionid + '\'' +
                '}';
    }
}
