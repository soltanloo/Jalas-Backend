package DataManagers;

import Models.PollOption;
import java.sql.*;


/*
    private int id;
    private ArrayList<String> userList = new ArrayList<>();
    private String timeOption;
 */
public class PollOptionDataHandler {
    private static Connection con = null;

    private static final String COLUMNS = "(id, userList, startTime, finishTime)";

    public static void init() {
        try {
            DataManager.dropExistingTable("PollOption");
            con = DataBaseConnector.getConnection();
            Statement st = con.createStatement();

            String sql = "CREATE TABLE " +
                    "PollOption " +
                    "(id INTEGER PRIMARY KEY," +
                    "userList TEXT , " +
                    "startTime TEXT , " +
                    "finishTime TEXT)";
            st.executeUpdate(sql);

            st.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean addOption(PollOption option) {
        String sql = "INSERT INTO PollOption " + COLUMNS + " VALUES (?, ?, ?, ?)";

        try{
            con = DataBaseConnector.getConnection();
            PreparedStatement st = con.prepareStatement(sql);

            PollOptionDomainToDB(option, st);
            st.executeUpdate();
            st.close();
            con.close();
        }catch(SQLException se){
            se.printStackTrace();
            return false;
        }
        return true;
    }

    public static PollOption getPollOption(int id) {
        String sql = "SELECT * FROM PollOption WHERE id = ?";

        try {
            PollOption option = null;
            con = DataBaseConnector.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getString(1).equals(id))
                    option = PollOptionDBtoDomain(rs);
            }
            if (option == null)
                return null;

            stmt.close();
            rs.close();
            con.close();
            return option;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateUserIDList(PollOption pollOption) {
        String sql = "UPDATE PollOption SET userList = ? where id = ?";

        try {
            con = DataBaseConnector.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, DataHelpers.stringify(pollOption.getUserList()));
            st.setInt(2, pollOption.getId());
            st.executeUpdate();
            st.close();
            con.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
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
