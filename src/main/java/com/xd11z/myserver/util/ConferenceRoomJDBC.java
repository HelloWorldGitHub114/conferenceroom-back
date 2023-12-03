package com.xd11z.myserver.util;

import com.xd11z.myserver.entity.ConferenceRoom;

import java.sql.*;
import java.util.*;

public class ConferenceRoomJDBC
{
    /**
     * 查询数据库中所有的会议室信息
     * @param SearchState 搜索状态：搜索全搜索/可用的/不可用的 会议室
     * @return List<ConferenceRoom> 所有会议室信息
     */
    public static List<ConferenceRoom> SearchAll(int SearchState)
    {
        List<ConferenceRoom> res = new ArrayList<ConferenceRoom>();
        String QUERY_ROOM;
        if(SearchState==0) QUERY_ROOM = "SELECT * FROM ConferenceRoom";
        else QUERY_ROOM = "SELECT * FROM ConferenceRoom where used = 1";
        Connection conn = null;
        PreparedStatement stmtRoom = null;
        ResultSet rsRooms = null;
        try {
            conn = DriverManager.getConnection(JDBCconnection.connectionurl);
            stmtRoom = conn.prepareStatement(QUERY_ROOM);
            rsRooms= stmtRoom.executeQuery();
            while(rsRooms.next())
            {
                String roomNo = rsRooms.getString("RoomID");
                String roomName = rsRooms.getString("RoomName");
                String roomFloor = rsRooms.getString("RoomFloor");
                String roomType = rsRooms.getString("RoomType");
                int roomSize = rsRooms.getInt("Size");
                int roomArea = rsRooms.getInt("Area");
                int roomState = rsRooms.getInt("used");
                ConferenceRoom R = new ConferenceRoom(roomNo,roomName,roomFloor,roomType,roomSize,roomArea,roomState);
                res.add(R);
            }
        }
        catch (SQLException se)
        {
            se.printStackTrace();
        }
        finally
        {
            try
            {
                if (rsRooms != null) rsRooms.close();
                if (stmtRoom != null) stmtRoom.close();
                if (conn != null) conn.close();
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }
        }
        return res;
    }

    /**
     * 查询数据库中所有的会议室信息
     * @param roomFloor 楼层
     * @param roomType 类型
     * @param roomSize 容量（人数）
     * @return List<ConferenceRoom> 符合条件的会议室信息
     */
    public static List<ConferenceRoom> SearchOnConditon(String roomFloor,String roomType,String roomSize)
    {
        List<ConferenceRoom> res = new ArrayList<ConferenceRoom>();
        String QUERY_ROOM = "SELECT * FROM ConferenceRoom";
        boolean hasCondition = false;
        try (Connection connection = DriverManager.getConnection(JDBCconnection.connectionurl))
        {
            if (!roomFloor.isEmpty() || !roomType.isEmpty() || !roomSize.isEmpty())  QUERY_ROOM += " WHERE";
            if (!roomFloor.isEmpty())
            {
                QUERY_ROOM += " roomFloor = ?";
                hasCondition = true;
            }
            if (!roomType.isEmpty())
            {
                if (hasCondition)  QUERY_ROOM += " AND roomType = ?";
                else
                {
                    QUERY_ROOM += " roomType = ?";
                    hasCondition = true;
                }
            }
            if (!roomSize.isEmpty())
            {
                if (hasCondition) QUERY_ROOM += " AND Size = ?";
                else QUERY_ROOM += " Size = ?";
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_ROOM))
            {
                int parameterIndex = 1;
                if (!roomFloor.isEmpty()) preparedStatement.setString(parameterIndex++, roomFloor);
                if (!roomType.isEmpty()) preparedStatement.setString(parameterIndex++, roomType);
                if (!roomSize.isEmpty()) preparedStatement.setString(parameterIndex, roomSize);
                try (ResultSet rsRooms = preparedStatement.executeQuery())
                {
                    while (rsRooms.next())
                    {
                        String roomNo = rsRooms.getString("RoomID");
                        String roomName = rsRooms.getString("RoomName");
                        String rsroomFloor = rsRooms.getString("RoomFloor");
                        String rsroomType = rsRooms.getString("RoomType");
                        int rsroomSize = rsRooms.getInt("Size");
                        int roomArea = rsRooms.getInt("Area");
                        int roomState = rsRooms.getInt("used");
                        ConferenceRoom R = new ConferenceRoom(roomNo,roomName,rsroomFloor,rsroomType,rsroomSize,roomArea,roomState);
                        res.add(R);
                    }
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 管理员改变会议室的可用状况used
     * @param c 会议室实体
     * @return boolean 是否更改成功
     */
    public static boolean ChangeState(ConferenceRoom c)
    {
        String RoomID=c.roomNo;
        String UPDATEState = "Update ConferenceRoom set used=(1-used) Where RoomID= ? ";
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(JDBCconnection.connectionurl);
            stmt = conn.prepareStatement(UPDATEState);
            stmt.setString(1, RoomID);
            stmt.executeUpdate();
            return true;
        }
        catch (SQLException se)
        {
            se.printStackTrace();
            return false;
        }
        finally
        {
            try
            {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }
        }
    }
    /** 获取会议室的所有可能楼层，用于下拉检索框
     * @param SearchState 搜索状态：搜索全搜索/可用的/不可用的
     * @return List<Map<String,Integer>>，排好序的楼层
     */
    public static List<Map<String,Integer>> GetDistinctFloor(int SearchState)
    {
        List<Map<String,Integer>> res = new ArrayList<>();
        String QUERY_ROOM;
        if(SearchState==0) QUERY_ROOM = "SELECT DISTINCT RoomFloor FROM ConferenceRoom ORDER BY RoomFloor ASC;";
        else QUERY_ROOM = "SELECT DISTINCT RoomFloor FROM ConferenceRoom where used=1 ORDER BY RoomFloor ASC;";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            //开一个连接
            conn = DriverManager.getConnection(JDBCconnection.connectionurl);
            stmt = conn.prepareStatement(QUERY_ROOM);
            rs= stmt.executeQuery();
            while(rs.next())
            {
                Map<String,Integer> mp= new HashMap<>();
                mp.put("roomFloor",Integer.parseInt(rs.getString("RoomFloor")));
                res.add(mp);
            }
        }
        catch (SQLException se)
        {
            se.printStackTrace();
        }
        finally
        {
            //关闭数据库连接
            try
            {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }
        }
        return res;
    }

    /** 获取会议室的所有可能类型，用于下拉检索框
     * @param SearchState 搜索状态：搜索全搜索/可用的/不可用的
     */
    public static List<Map<String,String>> GetDistinctType(int SearchState)
    {
        List<Map<String,String>> res = new ArrayList<>();
        String QUERY_ROOM;
        if(SearchState==0) QUERY_ROOM = "SELECT DISTINCT RoomType FROM ConferenceRoom";
        else QUERY_ROOM = "SELECT DISTINCT RoomType FROM ConferenceRoom where used=1";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            //开一个连接
            conn = DriverManager.getConnection(JDBCconnection.connectionurl);
            stmt = conn.prepareStatement(QUERY_ROOM);
            rs= stmt.executeQuery();
            while(rs.next())
            {
                Map<String,String> mp= new HashMap<>();
                mp.put("roomType",rs.getString("RoomType"));
                res.add(mp);
            }
        }
        catch (SQLException se)
        {
            se.printStackTrace();
        }
        finally
        {
            //关闭数据库连接
            try
            {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }
        }
        return res;
    }

    /** 获取会议室的所有可能人数（容量），用于下拉检索框
     * @param SearchState 搜索状态：搜索全搜索/可用的/不可用的
     */
    public static List<Map<String,Integer>> GetDistinctSize(int SearchState)
    {
        List<Map<String,Integer>> res = new ArrayList<>();
        String QUERY_ROOM;
        if(SearchState==0) QUERY_ROOM = "SELECT DISTINCT Size FROM ConferenceRoom ORDER BY Size ASC;";
        else QUERY_ROOM = "SELECT DISTINCT Size FROM ConferenceRoom where used=1 ORDER BY Size ASC;";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            //开一个连接
            conn = DriverManager.getConnection(JDBCconnection.connectionurl);
            stmt = conn.prepareStatement(QUERY_ROOM);
            rs= stmt.executeQuery();
            while(rs.next())
            {
                Map<String,Integer> mp= new HashMap<>();
                mp.put("roomSize",Integer.parseInt(rs.getString("Size")));
                res.add(mp);
            }
        }
        catch (SQLException se)
        {
            se.printStackTrace();
        }
        finally
        {
            //关闭数据库连接
            try
            {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            }
            catch (SQLException se)
            {
                se.printStackTrace();
            }
        }
        return res;
    }
}
