package com.xianglei.charge_service.common;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author xianglei
 *
 */
public class BaseJson {


    private boolean status = false; // 此接口返回的状态，正确是true、错误false
    private Integer code; // 返回代码，eg： -1 0 1 等，根据业务场景自定义
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data; // 返回到页面的数据
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object token;
    private String message;// 返回到页面的错误描述

    public Object getToken() {
        return token;
    }

    public void setToken(Object token) {
        this.token = token;
    }

    public BaseJson(){
    }

    public BaseJson(boolean status){
        this.setStatus(status);
        this.setMessage("");
    }

    public BaseJson(boolean status, String msg){
        this.setStatus(status);
        this.setMessage(msg);
    }
    public BaseJson(boolean successful, String msg, Object data) {
        this.status = successful;
        this.message = msg;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BaseJson [status=" + status + ", code=" + code + ", data="
                + data + ", message=" + message + "]";
    }

}
