package com.lock.demo.entity;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * @author Lock
 * @date 2018/4/30 22:21
 */

public class UserParam {

    private Integer id;

    @JsonProperty("username")
    private String userName;


    @JsonProperty("passwd")
    private String passwd;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
