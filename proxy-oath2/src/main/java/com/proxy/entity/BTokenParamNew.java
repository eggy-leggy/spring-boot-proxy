package com.proxy.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author : Frank F
 * @ClassName: TokenParamNew
 * @Descripsion : 新获取token 请求参数
 * @Date : 2019-09-04 22:50
 * @Version :  0.0.1
 */


public class BTokenParamNew {

    private String method;
    @JSONField(name = "grant_type")
    private String grantType;
    @JSONField(name = "client_id")
    private String clientId;
    @JSONField(name = "client_secret")
    private String clientSecret;
    private String username;
    private String password;
    private String version;
    private long timestamp;


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
