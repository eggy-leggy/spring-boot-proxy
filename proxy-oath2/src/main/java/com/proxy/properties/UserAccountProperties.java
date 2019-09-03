package com.proxy.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : Frank F
 * @ClassName: PubParamProperties
 * @Descripsion :
 * @Date : 2019-09-04 00:07
 * @Version :  0.0.1
 */


@ConfigurationProperties(prefix = "yf.account")
public class UserAccountProperties {

    private String appKey;
    private String appSecret;
    private String salt;
    private String userAccount;
    private String password;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
