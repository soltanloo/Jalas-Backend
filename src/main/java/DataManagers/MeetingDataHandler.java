package DataManagers;

import Models.Meeting;

import java.sql.*;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class MeetingDataHandler {
    private static Connection con = null;

    private static final String COLUMNS = "(id, roomNumber, startTime, finishTime, status, createTime, setTime)";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DDTHH:mm:ss");

    public static void init() {
        try {
            DataManager.dropExistingTable("Meeting");
            con = DataBaseConnector.getConnection();
            Statement st = con.createStatement();

            String sql = "CREATE TABLE " +
                    "Meeting " +
                    "(id INTEGER PRIMARY KEY, " +
                    "roomNumber INTEGER, " +
                    "startTime TEXT, " +
                    "finishTime TEXT, " +
                    "status INTEGER)";
            st.executeUpdate(sql);

            st.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean addMeeting(Meeting meeting) {
        String sql = "INSERT INTO Meeting " + COLUMNS + " VALUES (?, ?, ?, ?, ?, ?, ?)";

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

    public static void setMeetingStatus(Meeting meeting) {
        String sql = "UPDATE Meeting SET status = ? AND setTime = ? where id = ?";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        try {
            con = DataBaseConnector.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, meeting.getStatus().getLevelCode());
            st.setString(2, sdf.format(timestamp));
            st.setInt(3, meeting.getId());
            st.executeUpdate();
            st.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static int getSetMeetingsNum(){
        String sql = "SELECT * FROM Meeting WHERE status = ?";

        int meetingsSet = 0;
        try {
            con = DataBaseConnector.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, 1);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                meetingsSet += 1;
            }

            stmt.close();
            rs.close();
            con.close();
            return meetingsSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static int getCancelledMeetingsNum() {
        String sql = "SELECT * FROM Meeting WHERE status = ?";

        int meetingsCancelled = 0;
        try {
            con = DataBaseConnector.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, -1);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                meetingsCancelled += 1;
            }

            stmt.close();
            rs.close();
            con.close();
            return meetingsCancelled;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static ArrayList<Meeting> getStalledMeetings() {
        String sql = "SELECT * FROM Meeting WHERE status = ?";

        ArrayList<Meeting> meetings = new ArrayList<>();
        try {
            con = DataBaseConnector.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, 0);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                meetings.add(meetingDBtoDomain(rs));
            }

            stmt.close();
            rs.close();
            con.close();
            return meetings;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean cancelMeeting(int id) {
        String sql = "UPDATE Meeting SET status = ? where id = ?";
        try {
            con = DataBaseConnector.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, Meeting.Status.CANCELLED.getLevelCode());
            stmt.setInt(2, id);
            stmt.executeUpdate();
            stmt.close();
            con.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void meetingDomainToDB(Meeting meeting,  PreparedStatement st) {
        try {
            st.setInt(1, meeting.getId());
            st.setInt(2, meeting.getRoomNumber());
            st.setString(3, meeting.getStartTime());
            st.setString(4, meeting.getFinishTime());
            st.setInt(5, meeting.getStatus().getLevelCode());
            st.setString(6, meeting.getCreateTime());
            st.setString(7, meeting.getSetTime());
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
            meeting.setStatus(rs.getInt("status"));
            meeting.setCreateTime(rs.getString("createTime"));
            meeting.setSetTime(rs.getString("setTime"));
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return meeting;
    }

}
