package com.xd11z.myserver.util;
import com.xd11z.myserver.entity.*;
import java.sql.*;
import java.util.*;
import java.time.*;
    public class RecordJDBC {

        public static Integer PAGE_SIZE=7;

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
                    record.setApplyTime(resultSet.getString("ApplyTime"));
                    // 设置其他属性的值，类似...
                    record.setAuditTime(resultSet.getString("AuditTime"));
                    record.setRejectReason(resultSet.getString("RejectReason"));
                    record.setStartTime(resultSet.getString("StartTime"));
                    record.setEndTime(resultSet.getString("EndTime"));
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
                    record.setApplyTime(resultSet.getString("ApplyTime"));
                    record.setAuditTime(resultSet.getString("AuditTime"));
                    record.setRejectReason(resultSet.getString("RejectReason"));
                    record.setStartTime(resultSet.getString("StartTime"));
                    record.setEndTime(resultSet.getString("EndTime"));
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

        public static void updateAuditStatusRejection(Integer applyId, Integer auditState, String rejectReason) {
            Connection connection = null;
            PreparedStatement preparedStatement = null;

            try {
                connection = DriverManager.getConnection(JDBCconnection.connectionurl);

                String sql = "UPDATE Reservation SET AuditStatus = ?, RejectReason = ? WHERE ApplyId = ?";
                preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setInt(1, auditState);
                preparedStatement.setString(2, rejectReason);
                preparedStatement.setInt(3, applyId);

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


        public static List<Integer> searchTimeConflict(Integer roomId, LocalDateTime startTime, LocalDateTime endTime) {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            List<Integer> conflictIds = new ArrayList<>();

            try {
                connection = DriverManager.getConnection(JDBCconnection.connectionurl);

                String sql = "SELECT ApplyId FROM Reservation " +
                        "WHERE RoomId = ? AND AuditStatus = 1 AND " +
                        "((StartTime <= ? AND EndTime >= ?) OR (StartTime <= ? AND EndTime >= ?))";

                preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setInt(1, roomId);
                preparedStatement.setTimestamp(2, Timestamp.valueOf(startTime));
                preparedStatement.setTimestamp(3, Timestamp.valueOf(endTime));
                preparedStatement.setTimestamp(4, Timestamp.valueOf(startTime));
                preparedStatement.setTimestamp(5, Timestamp.valueOf(endTime));

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

    }


