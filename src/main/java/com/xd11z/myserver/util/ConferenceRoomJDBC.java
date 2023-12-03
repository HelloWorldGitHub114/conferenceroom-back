package com.xd11z.myserver.util;

import com.xd11z.myserver.entity.ConferenceRoom;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConferenceRoomJDBC
{
    /**
     * 查询数据库中所有的会议室信息
     * 注意连接字符串需要自己修改
     * @return List<ConferenceRoom> 所有会议室信息
     */
    public static List<ConferenceRoom> SearchAll()
    {
        List<ConferenceRoom> res = new ArrayList<ConferenceRoom>();
        String QUERY_ROOM = "SELECT * FROM ConferenceRoom";
        Connection conn = null;
        PreparedStatement stmtRoom = null;
        ResultSet rsRooms = null;
        try {
            //开一个连接
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;encrypt=false;databaseName=reserve;user=typemoon;password=Kama&L1lith");
            //检查预约用户
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
            //关闭数据库连接
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
     * 管理员改变会议室的可用状况used
     * @param c 接受会议室实体
     * @return boolean 是否更改成功
     */
    public static boolean ChangeState(ConferenceRoom c)
    {
        String RoomID=c.roomNo;
        String UPDATEState = "Update ConferenceRoom set used=(1-used) Where RoomID= ? ";
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;encrypt=false;databaseName=reserve;user=typemoon;password=Kama&L1lith");
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
            //关闭数据库连接
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
}
