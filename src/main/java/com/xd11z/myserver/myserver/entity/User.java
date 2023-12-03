package com.xd11z.myserver.myserver.entity;

import com.xd11z.myserver.entity.UserInfo;

import java.io.Serializable;

/**
 * 完整的用户类，用于和sql交互
 */
public class User implements Serializable {

    public String userID; //用户ID，这个不用返回给前端，仅用于后端处理，此处用于传状态码
    public String username; //用户名
    public String password; //密码
    public String role; //用户角色

    public User(String userID, String username, String password, String role) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * 返回用户信息，用于发送到前端
     * @return 用户信息数据，前端只需要拿这个
     */
    public UserInfo getInfo() {
        return new UserInfo(this.username,this.role);
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
