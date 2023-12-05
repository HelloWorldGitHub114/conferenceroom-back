package com.xd11z.myserver.util;

import com.xd11z.myserver.entity.ConferenceRoom;
import com.xd11z.myserver.entity.Device;
import java.sql.*;
import java.util.*;

public class ConferenceRoomJDBC
{
    /**
     * 查询数据库中所有的会议室信息
     * @param SearchState 0：不可用/1：可用的/2：全搜索
     * @return List<ConferenceRoom> 所有会议室信息
     */
    public static List<ConferenceRoom> SearchAll(int SearchState)
    {
        List<ConferenceRoom> res = new ArrayList<ConferenceRoom>();
        String QUERY_ROOM;
        if(SearchState==2) QUERY_ROOM = "SELECT * FROM ConferenceRoom";
        else if(SearchState==1) QUERY_ROOM = "SELECT * FROM ConferenceRoom where used = 1";
        else QUERY_ROOM = "SELECT * FROM ConferenceRoom where used = 0";
        Connection conn = null;
        PreparedStatement stmtRoom = null;
        ResultSet rsRooms = null;
        try {
            conn = DriverManager.getConnection(JDBCconnection.connectionurl);
            stmtRoom = conn.prepareStatement(QUERY_ROOM);
            rsRooms= stmtRoom.executeQuery();
            while(rsRooms.next())
            {
                int roomID = rsRooms.getInt("RoomID");
                String roomNo = rsRooms.getString("RoomNo");
                String roomName = rsRooms.getString("RoomName");
                int roomFloor = rsRooms.getInt("RoomFloor");
                int roomSize = rsRooms.getInt("Size");
                float roomArea = rsRooms.getFloat("Area");
                int roomState = rsRooms.getInt("used");
                ConferenceRoom R = new ConferenceRoom(roomID,roomNo,roomName,roomFloor,roomSize,roomArea,roomState);
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
     * 按条件查询数据库中所有的会议室信息
     * @param roomFloor 楼层
     * @param roomSize 容量（人数）
     * @param SearchState 0：不可用/1：可用的/2：全搜索
     * @return List<ConferenceRoom> 符合条件的会议室信息
     */
    public static List<ConferenceRoom> SearchOnConditon(String roomFloor,String roomSize,int SearchState)
    {
        List<ConferenceRoom> res = new ArrayList<ConferenceRoom>();
        String QUERY_ROOM = "SELECT * FROM ConferenceRoom";
        boolean hasCondition = false;
        try (Connection connection = DriverManager.getConnection(JDBCconnection.connectionurl))
        {
            if (!roomFloor.isEmpty()  || !roomSize.isEmpty() || SearchState!=2)  QUERY_ROOM += " WHERE";
            if (!roomFloor.isEmpty())
            {
                QUERY_ROOM += " roomFloor = ?";
                hasCondition = true;
            }
            if (!roomSize.isEmpty())
            {
                if (hasCondition) QUERY_ROOM += " AND Size = ?";
                else{
                    QUERY_ROOM += " Size = ?";
                    hasCondition = true;
                }
            }
            if(SearchState==0)
            {
                if (hasCondition) QUERY_ROOM += " AND used = 0";
                else {
                    QUERY_ROOM += " used = 0";
                }
            }
            else if(SearchState==1)
            {
                if (hasCondition) QUERY_ROOM += " AND used = 1";
                else {
                    QUERY_ROOM += " used = 1";
                }
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_ROOM))
            {
                int parameterIndex = 1;
                if (!roomFloor.isEmpty()) preparedStatement.setString(parameterIndex++, roomFloor);
                if (!roomSize.isEmpty()) preparedStatement.setString(parameterIndex, roomSize);
                try (ResultSet rsRooms = preparedStatement.executeQuery())
                {
                    while (rsRooms.next())
                    {
                        int roomID = rsRooms.getInt("RoomID");
                        String roomNo = rsRooms.getString("RoomNo");
                        String roomName = rsRooms.getString("RoomName");
                        int RsroomFloor = rsRooms.getInt("RoomFloor");
                        int RsroomSize = rsRooms.getInt("Size");
                        float roomArea = rsRooms.getFloat("Area");
                        int roomState = rsRooms.getInt("used");
                        ConferenceRoom R = new ConferenceRoom(roomID,roomNo,roomName,RsroomFloor,RsroomSize,roomArea,roomState);
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
        int RoomID=c.roomID;
        String UPDATEState = "Update ConferenceRoom set used=(1-used) Where RoomID= ? ";
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(JDBCconnection.connectionurl);
            stmt = conn.prepareStatement(UPDATEState);
            stmt.setInt(1, RoomID);
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
     * @param SearchState 0：不可用/1：可用的/2：全搜索
     * @return List<Map<String,Integer>>，排好序的楼层
     */
    public static List<Map<String,Integer>> GetDistinctFloor(int SearchState)
    {
        List<Map<String,Integer>> res = new ArrayList<>();
        String QUERY_ROOM;
        if(SearchState==2) QUERY_ROOM = "SELECT DISTINCT RoomFloor FROM ConferenceRoom ORDER BY RoomFloor ASC;";
        else if(SearchState==1) QUERY_ROOM = "SELECT DISTINCT RoomFloor FROM ConferenceRoom where used=1 ORDER BY RoomFloor ASC;";
        else QUERY_ROOM = "SELECT DISTINCT RoomFloor FROM ConferenceRoom where used=0 ORDER BY RoomFloor ASC;";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
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
     * @param SearchState 0：不可用/1：可用的/2：全搜索
     */
    public static List<Map<String,Integer>> GetDistinctSize(int SearchState)
    {
        List<Map<String,Integer>> res = new ArrayList<>();
        String QUERY_ROOM;
        if(SearchState==2) QUERY_ROOM = "SELECT DISTINCT Size FROM ConferenceRoom ORDER BY Size ASC;";
        else if(SearchState==1) QUERY_ROOM = "SELECT DISTINCT Size FROM ConferenceRoom where used=1 ORDER BY Size ASC;";
        else QUERY_ROOM = "SELECT DISTINCT Size FROM ConferenceRoom where used=0 ORDER BY Size ASC;";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
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

    public static boolean CheckRepeatID(int id)
    {
        String QUERY = "SELECT Count(*) as cnt FROM ConferenceRoom Where RoomID = ?";
        int res=0;
        try (Connection connection = DriverManager.getConnection(JDBCconnection.connectionurl))
        {
            try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY))
            {
                preparedStatement.setInt(1, id);
                try (ResultSet rs = preparedStatement.executeQuery())
                {
                    if (rs.next()) res=rs.getInt("cnt");
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return (res==1);
    }

    public static boolean CheckRepeatNo(String no)
    {
        String QUERY = "SELECT Count(*) as cnt FROM ConferenceRoom Where RoomNo = ?";
        int res=0;
        try (Connection connection = DriverManager.getConnection(JDBCconnection.connectionurl))
        {
            try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY))
            {
                preparedStatement.setString(1, no);
                try (ResultSet rs = preparedStatement.executeQuery())
                {
                    if (rs.next()) res=rs.getInt("cnt");
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return (res==1);
    }

    public static int GiveID()
    {
        String QUERY = "SELECT Max(roomID) as cnt FROM ConferenceRoom";
        int cnt=0;
        try (Connection connection = DriverManager.getConnection(JDBCconnection.connectionurl))
        {
            try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY))
            {
                try (ResultSet rs = preparedStatement.executeQuery())
                {
                    if (rs.next()) cnt=rs.getInt("cnt");
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return cnt;
    }

    public static boolean UpdateInfo(ConferenceRoom conferenceRoom)
    {
        String UPDATEState = """    
                    UPDATE ConferenceRoom
                    SET RoomNo = ?, RoomName = ?, RoomFloor = ?, Size = ?, Area = ? ,used = ?
                    WHERE RoomID = ?;
                    """;
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(JDBCconnection.connectionurl);
            stmt = conn.prepareStatement(UPDATEState);
            stmt.setString(1,conferenceRoom.roomNo);
            stmt.setString(2,conferenceRoom.roomName);
            stmt.setInt(3,conferenceRoom.roomFloor);
            stmt.setInt(4,conferenceRoom.roomSize);
            stmt.setFloat(5,conferenceRoom.roomArea);
            stmt.setInt(6,conferenceRoom.roomState);
            stmt.setInt(7,conferenceRoom.roomID);
            stmt.executeUpdate();
            return true;
        }
        catch (SQLException se)
        {
            se.printStackTrace();
            return false;
        }
    }

    public static boolean InsertInfo(ConferenceRoom conferenceRoom)
    {
        int affectedRow=0;
        String UPDATEState ="INSERT INTO ConferenceRoom VALUES (?,?,?,?,?,?,null,null,?)";
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(JDBCconnection.connectionurl);
            stmt = conn.prepareStatement(UPDATEState);
            stmt.setInt(1,GiveID()+1);
            stmt.setString(2,conferenceRoom.roomNo);
            stmt.setString(3,conferenceRoom.roomName);
            stmt.setInt(4,conferenceRoom.roomFloor);
            stmt.setInt(5,conferenceRoom.roomSize);
            stmt.setFloat(6,conferenceRoom.roomArea);
            stmt.setInt(7,conferenceRoom.roomState);
            affectedRow=stmt.executeUpdate();
        }
        catch (SQLException se)
        {
            se.printStackTrace();
        }
        return (!(affectedRow==0));
    }

    public static ConferenceRoom SearchByID(int id)
    {
        ConferenceRoom res = null;
        String QUERY_ROOM = "SELECT * FROM ConferenceRoom where RoomID = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(JDBCconnection.connectionurl);
            stmt = conn.prepareStatement(QUERY_ROOM);
            stmt.setInt(1,id);
            rs= stmt.executeQuery();
            if (rs.next())
            {
                int roomID = rs.getInt("RoomID");
                String roomNo = rs.getString("RoomNo");
                String roomName = rs.getString("RoomName");
                int roomFloor = rs.getInt("RoomFloor");
                int roomSize = rs.getInt("Size");
                float roomArea = rs.getFloat("Area");
                int roomState = rs.getInt("used");
                res = new ConferenceRoom(roomID,roomNo,roomName,roomFloor,roomSize,roomArea,roomState);
            }
        }
        catch (SQLException se)
        {
            se.printStackTrace();
        }
        return res;
    }

    public static boolean DeleteRoom(int id)
    {
        int affectedRow = 0;
        String QUERY_ROOM = "Delete FROM ConferenceRoom where RoomID = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(JDBCconnection.connectionurl);
            stmt = conn.prepareStatement(QUERY_ROOM);
            stmt.setInt(1,id);
            affectedRow = stmt.executeUpdate();
        }
        catch (SQLException se)
        {
            se.printStackTrace();
        }
        return (affectedRow==1);
    }
}
