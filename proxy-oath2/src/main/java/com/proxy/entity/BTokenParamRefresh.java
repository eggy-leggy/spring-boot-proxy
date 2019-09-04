package com.proxy.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author : Frank F
 * @ClassName: TokenParamRefresh
 * @Descripsion : 使用refresh token 刷新token  请求参数
 * @Date : 2019-09-04 22:50
 * @Version :  0.0.1
 */


public class BTokenParamRefresh {

    @JSONField(name = "client_id")
    private String clientId;
    @JSONField(name = "client_secret")
    private String clientSecret;
    @JSONField(name = "grant_type")
    private String grantType;
    @JSONField(name = "refresh_token")
    private String refreshToken;


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
