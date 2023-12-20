package com.xd11z.myserver.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        String QUERYUSERS = "SELECT * FROM Users WHERE Username = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int role = -1;
        try {
            //开一个连接
            conn = DriverManager.getConnection(JDBCconnection.connectionurl);
            stmt = conn.prepareStatement(QUERYUSERS);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next())//进到这里面说明用户名存在
            {
                role = rs.getInt("Role");
                if (rs.getString("Password").equals(password))//密码匹配
                {
                    return role;
                }
                else return 3;
            }
            else return 2;//用户名不存在
        }
        catch (SQLException se)
        {
            se.printStackTrace();
            return -1; //别的奇怪的错误返回-1
        } 
        finally 
        {
            //关闭数据库连接
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static String SearchID(String username, String password)
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String ID = "";
        int res=0;
        try {
            connection = DriverManager.getConnection(JDBCconnection.connectionurl);
            // 构建 SQL 查询语句
            String query="SELECT UserID FROM Users WHERE Username = ? And Password = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            // 执行查询
            resultSet = preparedStatement.executeQuery();
            // 处理查询结果
            if(resultSet.next()) ID=resultSet.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
            // 处理异常，返回空列表或者抛出自定义异常，具体情况根据需求而定
        } finally {
            // 关闭数据库连接
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ID;
    }

}