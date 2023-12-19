package com.xd11z.myserver.util;

import com.xd11z.myserver.entity.User;
import com.xd11z.myserver.entity.UserLogin;

/**
 * 用于实现User
 */
public class UserLoginJDBC {
    /**
     * 用UserLogin判断用户登录信息是否正确，返回User类
     * @param userLogin 用户登录信息
     * @param msg  错误的时候可以把错误信息输入到msg里
     * @return 找到的用户，找不到返回null
     */
    public static User CheckUserLogin(UserLogin userLogin, StringBuilder msg)
    {
        int stat= LoginJDBC.checkUserType(userLogin.username,userLogin.password);//调用jdbc程序 获取返回值
        User res=null;
        if(stat==0)
        {
            String ID=LoginJDBC.SearchID(userLogin.username,userLogin.password);
            res=new User(ID,userLogin.username,userLogin.password,"user");
        }
        else if(stat==1)
        {
            String ID=LoginJDBC.SearchID(userLogin.username,userLogin.password);
            res=new User(ID,userLogin.username,userLogin.password,"admin");
        }
        else if(stat==2) msg.append("用户名不存在！");
        else if(stat==3) msg.append("用户名与密码不匹配！");
        else msg.append("连接数据库失败！");
        return res;
    }
}