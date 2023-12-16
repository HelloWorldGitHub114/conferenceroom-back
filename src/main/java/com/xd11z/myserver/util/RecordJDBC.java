package com.xd11z.myserver.util;
import com.xd11z.myserver.entity.*;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.*;
public class RecordJDBC {

    public static Integer PAGE_SIZE=6;

    public static List<ConRApplyRecord> listAllRecordsByAuditStatus(int auditStatus, Integer currentPage) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<ConRApplyRecord> records = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(JDBCconnection.connectionurl);

            // 编写 SQL 查询语句
            String query = "SELECT * FROM Reservation WHERE AuditStatus = ? ORDER BY ApplyId OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, auditStatus);
            preparedStatement.setInt(2, (currentPage - 1) * PAGE_SIZE);
            preparedStatement.setInt(3, PAGE_SIZE);

            // 执行查询
            resultSet = preparedStatement.executeQuery();

            // 处理查询结果
            while (resultSet.next()) {
                ConRApplyRecord record = new ConRApplyRecord();
                // 设置 record 的属性值，根据数据库表的字段名调整
                record.setApplyId(resultSet.getInt("ApplyId"));
                record.setAuditStatus(resultSet.getInt("AuditStatus"));
                record.setApplyTime(StringUtil.removePointZero(resultSet.getString("ApplyTime")));
                // 设置其他属性的值，类似...
                record.setAuditTime(StringUtil.removePointZero(resultSet.getString("AuditTime")));
                record.setRejectReason(resultSet.getString("RejectReason"));
                record.setStartTime(StringUtil.removePointZero(resultSet.getString("StartTime")));
                record.setEndTime(StringUtil.removePointZero(resultSet.getString("EndTime")));
                record.setTheme(resultSet.getString("Theme"));
                record.setPersonCount(resultSet.getInt("PersonCount"));
                record.setDigest(resultSet.getString("MeetingDigest"));
                record.setroomID(resultSet.getInt("RoomId"));
                record.setRoomNo(resultSet.getString("RoomNo"));
                record.setRoomFloor(resultSet.getInt("RoomFloor"));
                record.setRoomName(resultSet.getString("RoomName"));
                record.setDeleted(resultSet.getInt("IsDeleted"));
                record.setUserID(resultSet.getString("UserID"));
                // 将 record 添加到列表
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 在出现异常时返回空列表或者抛出自定义异常，具体情况根据需求而定
            return records;
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

        return records;
    }

    public static List<ConRApplyRecord> listByConditions(int auditState, int currentPage, int deleted) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<ConRApplyRecord> records = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(JDBCconnection.connectionurl);
            // 构建 SQL 查询语句
            String query = "SELECT * FROM Reservation WHERE AuditStatus = ? AND IsDeleted = ? ORDER BY ApplyId OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, auditState);
            preparedStatement.setInt(2, deleted);
            preparedStatement.setInt(3, (currentPage - 1) * PAGE_SIZE);  // Assuming PAGE_SIZE is a constant
            preparedStatement.setInt(4, PAGE_SIZE);
            // 执行查询
            resultSet = preparedStatement.executeQuery();
            // 处理查询结果
            while (resultSet.next()) {
                ConRApplyRecord record = new ConRApplyRecord();
                record.setApplyId(resultSet.getInt("ApplyId"));
                record.setAuditStatus(resultSet.getInt("AuditStatus"));
                record.setApplyTime(StringUtil.removePointZero(resultSet.getString("ApplyTime")));
                record.setAuditTime(StringUtil.removePointZero(resultSet.getString("AuditTime")));
                record.setRejectReason(resultSet.getString("RejectReason"));
                record.setStartTime(StringUtil.removePointZero(resultSet.getString("StartTime")));
                record.setEndTime(StringUtil.removePointZero(resultSet.getString("EndTime")));
                record.setTheme(resultSet.getString("Theme"));
                record.setPersonCount(resultSet.getInt("PersonCount"));
                record.setDigest(resultSet.getString("MeetingDigest"));
                record.setroomID(resultSet.getInt("RoomId"));
                record.setRoomNo(resultSet.getString("RoomNo"));
                record.setRoomFloor(resultSet.getInt("RoomFloor"));
                record.setRoomName(resultSet.getString("RoomName"));
                record.setDeleted(resultSet.getInt("IsDeleted"));
                record.setUserID(resultSet.getString("UserID"));
                // 将 record 添加到列表
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 处理异常，返回空列表或者抛出自定义异常，具体情况根据需求而定
            return records;
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
        return records;
    }

    public static int getTotalnumber(int auditState, int deleted) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int res=0;
        try {
            connection = DriverManager.getConnection(JDBCconnection.connectionurl);
            // 构建 SQL 查询语句
            String query;
            if(deleted==0) query="SELECT count(*) as cnt FROM Reservation WHERE AuditStatus = ? AND IsDeleted = 0 ";
            else if(deleted==1) query="SELECT count(*) as cnt FROM Reservation WHERE AuditStatus = ? AND IsDeleted = 1 ";
            else query="SELECT count(*) as cnt FROM Reservation WHERE AuditStatus = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, auditState);
            // 执行查询
            resultSet = preparedStatement.executeQuery();
            // 处理查询结果
            if(resultSet.next()) res=resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            // 处理异常，返回空列表或者抛出自定义异常，具体情况根据需求而定
            return res;
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
        return res;
    }
    public static void updateAuditStatus(Integer applyId, Integer auditStatus) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(JDBCconnection.connectionurl);

            String sql = "UPDATE Reservation SET AuditStatus = ? WHERE ApplyId = ?";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, auditStatus);
            preparedStatement.setInt(2, applyId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();  // 实际应用中应该有更合适的错误处理
        } finally {
            // 关闭资源
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();  // 实际应用中应该有更合适的错误处理
            }
        }
    }

    public static int updateAuditStatusRejection(int applyId, int auditStatus, String rejectReason) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(JDBCconnection.connectionurl);

            // 更新审核状态、审核时间和驳回理由
            String sql = "UPDATE Reservation SET AuditStatus = ?, AuditTime = GETDATE(), RejectReason = ? WHERE ApplyId = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, auditStatus);
            preparedStatement.setString(2, rejectReason);
            preparedStatement.setInt(3, applyId);

            // 执行更新
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();  // 实际应用中应该有更合适的错误处理
            return 0;  // 返回 0 表示更新失败
        } finally {
            // 关闭资源
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();  // 实际应用中应该有更合适的错误处理
            }
        }
    }


    public static List<Integer> searchTimeConflict(Integer roomId, LocalDateTime startTime, LocalDateTime endTime) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Integer> conflictIds = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(JDBCconnection.connectionurl);

            String sql = "SELECT ApplyId FROM Reservation " +
                    "WHERE RoomId = ? AND AuditStatus = 1 AND " +
                    "((StartTime <= ? AND ? < EndTime) OR (StartTime < ? AND ? <= EndTime) OR (? <= StartTime AND EndTime <= ?))";
            //注意这里的等号！有的端点重合是不影响的，有的影响！

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, roomId);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(startTime));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(startTime));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(endTime));
            preparedStatement.setTimestamp(5, Timestamp.valueOf(endTime));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(startTime));
            preparedStatement.setTimestamp(7, Timestamp.valueOf(endTime));

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                conflictIds.add(resultSet.getInt("ApplyId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();  // 实际应用中应该有更合适的错误处理
        } finally {
            // 关闭资源
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();  // 实际应用中应该有更合适的错误处理
            }
        }

        return conflictIds;
    }

    public static int deleteRecordById(Integer applyId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(JDBCconnection.connectionurl);

            String sql = "DELETE FROM Reservation WHERE ApplyId = ?";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, applyId);

            // 执行删除操作
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();  // 实际应用中应该有更合适的错误处理
            return 0;
        } finally {
            // 关闭资源
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();  // 实际应用中应该有更合适的错误处理
            }
        }
    }

    public static int updateAuditStatusAndTime(int applyId, int auditStatus) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(JDBCconnection.connectionurl);

            // 更新审核状态和审核时间
            String sql = "UPDATE Reservation SET AuditStatus = ?, AuditTime = GETDATE() WHERE ApplyId = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, auditStatus);
            preparedStatement.setInt(2, applyId);

            // 执行更新
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();  // 实际应用中应该有更合适的错误处理
            return 0;  // 返回 0 表示更新失败
        } finally {
            // 关闭资源
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();  // 实际应用中应该有更合适的错误处理
            }
        }
    }

    public static int getTotalnumberbyuser(String userID, int auditState) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(JDBCconnection.connectionurl);

            String sql = "SELECT COUNT(*) AS total FROM Reservation WHERE UserID = ? AND AuditStatus = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userID);
            preparedStatement.setInt(2, auditState);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }

            return 0;  // 如果没有找到记录，返回0
        } catch (SQLException e) {
            e.printStackTrace();  // 应该实现实际的错误处理
            return -1;  // 发生错误时返回-1
        } finally {
            // 关闭资源
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();  // 应该实现实际的错误处理
            }
        }
    }

    public static List<ConRApplyRecord> listByConditionsForUsers(String userID, Integer auditState, Integer currentPage) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<ConRApplyRecord> records = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(JDBCconnection.connectionurl);

            String sql = "SELECT * FROM Reservation " +
                    "WHERE UserID = ? AND AuditStatus = ? AND IsDeleted = 0 " +
                    "ORDER BY " +
                    "CASE " +
                    "   WHEN AuditStatus = 0 THEN 0 " +  // 未审核
                    "   WHEN AuditStatus = 1 THEN 1 " +  // 已批准
                    "   WHEN AuditStatus = 2 THEN 2 " +  // 已驳回
                    "END, " +
                    "ApplyTime DESC " +  // 申请时间降序
                    "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userID);
            preparedStatement.setInt(2, auditState);
            preparedStatement.setInt(3, (currentPage - 1) * PAGE_SIZE); // OFFSET
            preparedStatement.setInt(4, PAGE_SIZE); // FETCH NEXT

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ConRApplyRecord record = new ConRApplyRecord(resultSet);
                record.setAuditTime(StringUtil.removePointZero(resultSet.getString("AuditTime")));
                record.setApplyTime(StringUtil.removePointZero(resultSet.getString("ApplyTime")));
                record.setStartTime(StringUtil.removePointZero(resultSet.getString("StartTime")));
                record.setEndTime(StringUtil.removePointZero(resultSet.getString("EndTime")));
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();  // 在真实应用中适当地处理此异常
        } finally {
            // 关闭资源（结果集、语句和连接）
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();  // 在真实应用中适当地处理此异常
            }
        }

        return records;
    }



    public static int deleteRecordByUser(Integer applyId)
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection(JDBCconnection.connectionurl);
            String sql ="""
                            UPDATE Reservation
                            SET IsDeleted = 1
                            WHERE ApplyId = ?;
                            """;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, applyId);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    // 获取当前最大的ApplyId
    private static int getMaxApplyId(Connection connection) throws SQLException {
        int maxApplyId = 0;

        String sql = "SELECT MAX(ApplyId) AS maxApplyId FROM Reservation";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                maxApplyId = resultSet.getInt("maxApplyId");
            }
        }

        return maxApplyId;
    }
    // 添加申请记录
    public static int addRecord(ConRApplyRecord record) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(JDBCconnection.connectionurl);

            // 获取当前最大的ApplyId
            int maxApplyId = getMaxApplyId(connection);

            // 生成新的ApplyId
            int newApplyId = maxApplyId + 1;

            // 打印所有属性值
            System.out.println("New ApplyId: " + newApplyId);

            // 使用反射获取所有属性
            Field[] fields = ConRApplyRecord.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(record);
                    System.out.println(field.getName() + ": " + value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            // 查询会议室信息
            String roomInfoQuery = "SELECT RoomNo, RoomFloor, RoomName FROM ConferenceRoom WHERE RoomID = ?";
            preparedStatement = connection.prepareStatement(roomInfoQuery);
            preparedStatement.setInt(1, record.getroomID());
            resultSet = preparedStatement.executeQuery();

            String roomNo = null;
            int roomFloor = 0;
            String roomName = null;

            if (resultSet.next()) {
                roomNo = resultSet.getString("RoomNo");
                roomFloor = resultSet.getInt("RoomFloor");
                roomName = resultSet.getString("RoomName");
            }

            // 获取当前时间
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String currentDateTimeStr = currentDateTime.format(formatter);

            // 获取日期部分
            String selectDate = record.getselectDate();

            // 设置开始时间和结束时间
            String startTimeStr = selectDate + " " + record.getStartTime();
            String endTimeStr = selectDate + " " + record.getEndTime();

            // 插入申请记录
            String insertRecordQuery = "INSERT INTO Reservation (ApplyId, AuditStatus, ApplyTime, AuditTime, RejectReason, " +
                    "StartTime, EndTime, Theme, PersonCount, MeetingDigest, UserID, RoomId, RoomNo, RoomFloor, RoomName, IsDeleted, selectDate) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(insertRecordQuery);
            preparedStatement.setInt(1, newApplyId);
            preparedStatement.setInt(2, 0); // 申请状态置为0，表示未审批
            preparedStatement.setString(3, currentDateTimeStr);  // 使用当前时间作为申请时间
            preparedStatement.setString(4, null); // 审核时间置为null
            preparedStatement.setString(5, null); // 拒绝原因置为null
            preparedStatement.setString(6, startTimeStr);
            preparedStatement.setString(7, endTimeStr);
            preparedStatement.setString(8, record.getTheme());
            preparedStatement.setInt(9, record.getPersonCount());
            preparedStatement.setString(10, record.getDigest());
            preparedStatement.setString(11, record.getUserID());
            preparedStatement.setInt(12, record.getroomID());
            preparedStatement.setString(13, roomNo);
            preparedStatement.setInt(14, roomFloor);
            preparedStatement.setString(15, roomName);
            preparedStatement.setInt(16, 0); // IsDeleted 置为0
            preparedStatement.setString(17, record.getselectDate());
            preparedStatement.executeUpdate();
            return newApplyId;
        } catch (SQLException e) {
            e.printStackTrace();  // 在真实应用中适当地处理此异常
            return -1;  // 返回错误代码
        } finally {
            // 关闭资源
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();  // 在真实应用中适当地处理此异常
            }
        }
    }




}

