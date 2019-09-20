package com.proxy.app.enums;

/**
 * @author Frank F
 * @description: 与ESB同步状态
 * @create 2019-09-20 17:56
 */

public enum ESBSyncStatus {
    wait(0,"待同步"),
    failure(1,"同步失败"),
    succuss(2,"同步成功"),
    ;
    private int code;
    private String msg;

    private ESBSyncStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
