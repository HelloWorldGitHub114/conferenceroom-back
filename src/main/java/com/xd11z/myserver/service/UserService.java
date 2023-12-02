package com.xd11z.myserver.service;

import com.xd11z.myserver.data.User;
import com.xd11z.myserver.tool.UserTool;
import org.springframework.stereotype.Service;

/**
 * 用户服务类，我也不知道为啥要这么个服务，但是token验证好像要这么写
 */
@Service("UserService")
public class UserService {

    /**
     * 用userID查找用户，找不到返回null
     * @param userID 查找的用户ID
     * @return 找到的用户，找不到返回null
     */
    public User findUserById(String userID) {
        return UserTool.findUserByID(userID);
    }


    /**
     * 用username查找用户，找不到返回null
     * @param username 查找的用户名
     * @return 找到的用户，找不到返回null
     */
    public User findUserByName(String username){
        return UserTool.findUserByName(username);
    }
}