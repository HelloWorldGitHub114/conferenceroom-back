package com.xd11z.myserver.service;

import com.xd11z.myserver.entity.User;
import com.xd11z.myserver.entity.UserLogin;
import com.xd11z.myserver.util.LoginJDBC;
import org.springframework.stereotype.Service;

/**
 * 用户服务类，用于实现User相关业务
 */
@Service("UserService")
public class UserService {
    /**
     * 用UserLogin判断用户登录信息是否正确，返回User类
     * @param userLogin 用户登录信息
     * @param msg  错误的时候可以把错误信息输入到msg里
     * @return 找到的用户，找不到返回null
     */
    public User CheckUserLogin(UserLogin userLogin, StringBuilder msg)
    {
        int stat= LoginJDBC.checkUserType(userLogin.username,userLogin.password);//调用jdbc程序 获取返回值
        User res=null;
        if(stat==0)
        {
            String ID=LoginJDBC.SearchUserID(userLogin.username,userLogin.password);
            res=new User(ID,userLogin.username,userLogin.password,"user");
        }
        else if(stat==1)
        {
            String ID=LoginJDBC.SearchAdminID(userLogin.username,userLogin.password);
            res=new User(ID,userLogin.username,userLogin.password,"admin");
        }
        else if(stat==2) msg.append("用户名不存在！");
        else if(stat==3) msg.append("用户名与密码不匹配！");
        else msg.append("未知错误！");
        return res;
    }

    /**
     * 用userID查找用户，找不到返回null
     * @param userID 查找的用户ID
     * @return 找到的用户，找不到返回null
     */
    public User findUserByID(String userID){
        //这里应该配合数据库查找一下
        return new User("123","admin","123456","admin");
    }

    /**
     * 用username查找用户，找不到返回null
     * @param username 查找的用户名
     * @return 找到的用户，找不到返回null
     */
    public User findUserByName(String username) {
        //这里应该配合数据库查找一下
        return new User("123","admin","123456","admin");
    }
}