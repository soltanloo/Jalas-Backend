package DataManagers;

import Models.Meeting;

import java.sql.*;

public class MeetingDataHandler {
    private static Connection con = null;

    private static final String COLUMNS = "(id, roomNumber, startTime, finishTime)";

    public static void init() {
        try {
            DataManager.dropExistingTable("Meeting");
            con = DataBaseConnector.getConnection();
            Statement st = con.createStatement();

            String sql = "CREATE TABLE " +
                    "Meeting " +
                    "(id INTEGER PRIMARY KEY," +
                    "roomNumber INTEGER , " +
                    "startTime TEXT, " +
                    "finishTime TEXT";
            st.executeUpdate(sql);

            st.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateRoomNumber(int roomNumber, int id) {
        String sql = "UPDATE Meeting SET roomNumber = ? where id = ?";

        try {
            con = DataBaseConnector.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, roomNumber);
            st.setInt(2, id);
            st.executeUpdate();
            st.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean addMeeting(Meeting meeting) {
        String sql = "INSERT INTO Meeting " + COLUMNS + " VALUES (?, ?, ?, ?)";

        try{
            con = DataBaseConnector.getConnection();
            PreparedStatement st = con.prepareStatement(sql);

            meetingDomainToDB(meeting, st);
            st.executeUpdate();
            st.close();
            con.close();
        }catch(SQLException se){
            se.printStackTrace();
            return false;
        }
        return true;
    }

    public static Meeting getMeeting (Integer id) {
        String sql = "SELECT * FROM Meeting WHERE id = ?";
        try {
            Meeting meeting = null;
            con = DataBaseConnector.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getString(1).equals(id))
                    meeting = meetingDBtoDomain(rs);
            }
            if (meeting == null)
                return null;

            stmt.close();
            rs.close();
            con.close();
            return meeting;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void meetingDomainToDB(Meeting meeting,  PreparedStatement st) {
        try {
            st.setInt(1, meeting.getId());
            st.setInt(2, meeting.getRoomNumber());
            st.setString(3, meeting.getStartTime());
            st.setString(4, meeting.getFinishTime());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Meeting meetingDBtoDomain(ResultSet rs) {
        Meeting meeting = new Meeting();
        try {
            meeting.setId(rs.getInt("id"));
            meeting.setRoomNumber(rs.getInt("roomNumber"));
            meeting.setStartTime(rs.getString("startTime"));
            meeting.setFinishTime(rs.getString("finnishTime"));
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return meeting;
    }

}
