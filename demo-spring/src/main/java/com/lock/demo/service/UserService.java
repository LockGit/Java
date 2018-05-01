package com.lock.demo.service;

import org.apache.catalina.User;

/**
 * @author Lock
 * @date 2018/4/30 23:57
 */

public class UserService {

    /**
     * @instance UserService
     */
    public static UserService instance = new UserService();


    /**
     * 新增用户
     *
     * @param userName
     * @param passWd
     * @return
     */
    public String addUser(String userName, String passWd) {
        return userName + passWd;
    }


    /**
     * 登录
     *
     * @param userName
     * @param passWd
     * @return
     */
    public String login(String userName, String passWd) {
        if (userName.equals("admin") && passWd.equals("admin")) {
            return "ce9510776db452152bcc4de6b86999";
        }
        return "";
    }

}
