package com.xd11z.myserver.tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//本代码连接SQL Server，在用户登陆功能中被调用
//注意连接字符串需要自己修改

public class LoginJDBC {
    /**
     * 接收用户名+密码 连接数据库验证是否匹配
     * 注意连接字符串需要自己修改
     * @param username 用户名
     * @param password  密码
     * @return IntValue 0->用户 1->管理员 2->用户名不存在 3->不匹配
     */
    public static int checkUserType(String username, String password)
    {
        String QUERY_RESERVATION_USERS = "SELECT * FROM ReservationUsers WHERE Username = ?";
        String QUERY_ADMINS = "SELECT * FROM Administrators WHERE Username = ?";
        Connection conn = null;
        PreparedStatement stmtReservationUsers = null;
        PreparedStatement stmtAdmins = null;
        ResultSet rsReservationUsers = null;
        ResultSet rsAdmins = null;
        try {
            //开一个连接
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;encrypt=false;databaseName=reserve;user=typemoon;password=Kama&L1lith");
            //检查预约用户
            stmtReservationUsers = conn.prepareStatement(QUERY_RESERVATION_USERS);
            stmtReservationUsers.setString(1, username);
            rsReservationUsers = stmtReservationUsers.executeQuery();
            if (rsReservationUsers.next()) {
                //进到这里面说明用户名存在
                //看看密码是否能匹配上，分别返回0和3
                if (rsReservationUsers.getString("Password").equals(password)) {
                    return 0; 
                } else {
                    return 3; 
                }
            }
            //检查管理员
            stmtAdmins = conn.prepareStatement(QUERY_ADMINS);
            stmtAdmins.setString(1, username);
            rsAdmins = stmtAdmins.executeQuery();
            if (rsAdmins.next()) {
                //进到这里面说明用户名存在
                //看看密码是否能匹配上，分别返回1和3
                if (rsAdmins.getString("Password").equals(password)) {
                    return 1; 
                } else {
                    return 3; 
                }
            }
            //用户名不存在的情况
            return 2;
        }
        catch (SQLException se) {
            se.printStackTrace();
            return -1; //别的奇怪的错误返回-1
        } 
        finally 
        {
            //关闭数据库连接
            try {
                if (rsReservationUsers != null) rsReservationUsers.close();
                if (stmtReservationUsers != null) stmtReservationUsers.close();
                if (rsAdmins != null) rsAdmins.close();
                if (stmtAdmins != null) stmtAdmins.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}