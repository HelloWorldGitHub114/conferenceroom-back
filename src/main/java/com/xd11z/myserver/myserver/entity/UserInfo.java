package com.xd11z.myserver.myserver.entity;

import java.io.Serializable;

/**
 * 回应前端登录请求的类，前端只要用户名和角色，完整数据存在User中，这个类只用于发送
 */
public class UserInfo implements Serializable {

    public String username; //用户名
    public String role; //用户角色

    public UserInfo(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
