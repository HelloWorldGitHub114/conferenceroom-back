package com.xd11z.myserver.myserver.service;

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
     * 用UserLogin判断用户登录信息是否正确，返回User类，成功失败信息在UserID中
     * @param userLogin 用户登录信息
     * @param msg  错误的时候可以把错误信息输入到msg里
     * @return 找到的用户，找不到返回null
     */
    public User CheckUserLogin(UserLogin userLogin, String msg)
    {
        int stat= LoginJDBC.checkUserType(userLogin.username,userLogin.password);//调用jdbc程序 获取返回值
        User res=null;
        if(stat==0) res=new User("0",userLogin.username,userLogin.password,"user");
        else if(stat==1) res=new User("1",userLogin.username,userLogin.password,"admin");
        else if(stat==2) res=new User("2",null,null,"");
        else if(stat==3) res=new User("3",null,null,"");
        else res=new User("-1",null,null,"");
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