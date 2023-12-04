package com.xd11z.myserver.util;

import com.xd11z.myserver.entity.ConferenceRoom;
import com.xd11z.myserver.entity.Device;
import java.sql.*;
import java.util.*;
public class DeviceJDBC {
    public static List<Device> listByRoomid(Integer roomId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Device> devices = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(JDBCconnection.connectionurl);

            // 编写 SQL 查询语句
            String query = "SELECT * FROM Device WHERE RoomID = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, roomId);

            // 执行查询
            resultSet = preparedStatement.executeQuery();

            // 处理查询结果
            while (resultSet.next()) {
                Device device = new Device();
                device.setId(resultSet.getInt("DeviceID"));
                device.setDname(resultSet.getString("DeviceName"));
                device.setDnumber(resultSet.getInt("DeviceNum"));
                device.setRoomId(resultSet.getInt("RoomID"));

                // 将设备对象添加到列表
                devices.add(device);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 在出现异常时返回空列表或者抛出自定义异常，具体情况根据需求而定
            return devices;
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

        return devices;
    }

    public static int updateDeviceNumber(int deviceId, int newNumber) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(JDBCconnection.connectionurl);

            // 构建 SQL 更新语句
            String updateQuery = "UPDATE Device SET DeviceNum = ? WHERE DeviceID = ?";
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, newNumber);
            preparedStatement.setInt(2, deviceId);

            // 执行更新操作
            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected;
        } catch (SQLException e) {
            e.printStackTrace();
            // 在出现异常时返回负值或者抛出自定义异常，具体情况根据需求而定
            return -1;
        } finally {
            // 关闭数据库连接
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static int insertDevice(Device device) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(JDBCconnection.connectionurl);

            // 查询当前表中指定 RoomID 的记录数量
            String countQuery = "SELECT COUNT(*) FROM Device WHERE RoomID = ?";
            preparedStatement = connection.prepareStatement(countQuery);
            preparedStatement.setInt(1, device.getRoomId());
            resultSet = preparedStatement.executeQuery();

            // 获取记录数量
            int recordCount = 0;
            if (resultSet.next()) {
                recordCount = resultSet.getInt(1);
            }

            // 新的 DeviceID 为记录数量 + 1
            device.setId(recordCount + 1);

            // 插入新的设备记录
            String insertQuery = "INSERT INTO Device (DeviceID, DeviceName, RoomID, DeviceNum) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, device.getId());
            preparedStatement.setString(2, device.getDname());
            preparedStatement.setInt(3, device.getRoomId());
            preparedStatement.setInt(4, device.getDnumber());

            // 执行插入操作
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // 在出现异常时返回负值或者抛出自定义异常，具体情况根据需求而定
            return -1;
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
    }



    public static int updateDevice(Device device) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(JDBCconnection.connectionurl);

            // 更新设备记录
            String updateQuery = "UPDATE Device SET DeviceNum = ? WHERE DeviceID = ?";
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, device.getDnumber());
            preparedStatement.setInt(2, device.getId());

            // 执行更新操作
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // 在出现异常时返回负值或者抛出自定义异常，具体情况根据需求而定
            return -1;
        } finally {
            // 关闭数据库连接
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
