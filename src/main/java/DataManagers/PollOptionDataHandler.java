package DataManagers;

import ErrorClasses.DataBaseErrorException;
import Models.PollOption;
import java.sql.*;

public class PollOptionDataHandler {
    private static final String COLUMNS = "(id, userList, startTime, finishTime)";

    public static void init() {
        DataManager.dropExistingTable("PollOption");
        Connection con = DataBaseConnector.getConnection();
        try {
            Statement st = con.createStatement();

            String sql = "CREATE TABLE " +
                    "PollOption " +
                    "(id INTEGER PRIMARY KEY," +
                    "userList TEXT , " +
                    "startTime TEXT , " +
                    "finishTime TEXT)";
            st.executeUpdate(sql);

            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DataBaseConnector.releaseConnection(con);
    }

    public static void addOption(PollOption option) {
        String sql = "INSERT INTO PollOption " + COLUMNS + " VALUES (?, ?, ?, ?)";
        Connection con = DataBaseConnector.getConnection();
        try{
            PreparedStatement st = con.prepareStatement(sql);

            PollOptionDomainToDB(option, st);
            st.executeUpdate();
            st.close();
        }catch(SQLException se){
            se.printStackTrace();
        }
        DataBaseConnector.releaseConnection(con);
    }

    public static PollOption getPollOption(int id) throws DataBaseErrorException {
        String sql = "SELECT * FROM PollOption WHERE id = ?";
        Connection con = DataBaseConnector.getConnection();
        try {
            PollOption option = null;
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                option = PollOptionDBtoDomain(rs);
            }
            if (option == null)
                return null;

            stmt.close();
            rs.close();
            DataBaseConnector.releaseConnection(con);
            return option;
        } catch (SQLException e) {
            e.printStackTrace();
            DataBaseConnector.releaseConnection(con);
            throw new DataBaseErrorException();
        }
    }

    public static void updateUserIDList(PollOption pollOption) throws DataBaseErrorException {
        String sql = "UPDATE PollOption SET userList = ? where id = ?";
        Connection con = DataBaseConnector.getConnection();
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, DataHelpers.stringify(pollOption.getUserList()));
            st.setInt(2, pollOption.getId());
            st.executeUpdate();
            st.close();
            DataBaseConnector.releaseConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
            DataBaseConnector.releaseConnection(con);
            throw new DataBaseErrorException();
        }
    }

    public static void PollOptionDomainToDB(PollOption option,  PreparedStatement st) {
        try {
            st.setInt(1, option.getId());
            st.setString(2, DataHelpers.stringify(option.getUserList()));
            st.setString(3, option.getStartTime());
            st.setString(4, option.getFinishTime());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static PollOption PollOptionDBtoDomain(ResultSet rs) {

        try {
            PollOption option = new PollOption(rs.getString("startTime"), rs.getString("finishTime"));
            option.setId(rs.getInt("id"));
            option.setUserList(DataHelpers.makeList(rs.getString("userList")));
            return option;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
