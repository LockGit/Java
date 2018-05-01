package com.lock.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Lock
 * @date 2018/4/30 18:08
 */
public class JsonResponse {
    private int code;

    private String msg;


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
