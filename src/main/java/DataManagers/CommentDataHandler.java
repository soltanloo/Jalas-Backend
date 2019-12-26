package DataManagers;


//private int id;
//private String text;
//private boolean isReply;
//private int commentedPollId;
//private int repliedCommentId = -1;
//private int commenterId;
//private ArrayList<Comment> repliedComments = new ArrayList<>();

import ErrorClasses.DataBaseErrorException;
import Models.Comment;

import java.sql.*;

public class CommentDataHandler {
    private static final String COLUMNS = "(id, containingText, isReply, commentedPollId, repliedCommentId, commenterId, repliedComments)";

    public static void init() {
        DataManager.dropExistingTable("Comment");
        Connection con = DataBaseConnector.getConnection();
        try {
            Statement st = con.createStatement();

            String sql = "CREATE TABLE " +
                    "Comment " +
                    "(id INTEGER PRIMARY KEY, " +
                    "containingText TEXT, " +
                    "isReply INTEGER, " +
                    "commentedPollId INTEGER, " +
                    "repliedCommentId INTEGER, " +
                    "commenterId INTEGER, " +
                    "repliedComments TEXT)";
            st.executeUpdate(sql);
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DataBaseConnector.releaseConnection(con);
    }

    public static void addComment(Comment comment) throws DataBaseErrorException {
        String sql = "INSERT INTO Comment " + COLUMNS + " VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection con = DataBaseConnector.getConnection();
        try{
            PreparedStatement st = con.prepareStatement(sql);

            commentDomainToDB(comment, st);
            st.executeUpdate();
            st.close();
        }catch(SQLException se){
            se.printStackTrace();
            DataBaseConnector.releaseConnection(con);
            throw new DataBaseErrorException();
        }
        DataBaseConnector.releaseConnection(con);
    }

    public static Comment getComment (int id) throws DataBaseErrorException {
        String sql = "SELECT * FROM Comment WHERE id = ?";
        Connection con = DataBaseConnector.getConnection();
        try {
            Comment comment = null;
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                comment = commentDBtoDomain(rs);
            }
            if (comment == null)
                return null;

            stmt.close();
            rs.close();
            DataBaseConnector.releaseConnection(con);
            return comment;
        } catch (SQLException e) {
            e.printStackTrace();
            DataBaseConnector.releaseConnection(con);
            throw new DataBaseErrorException();
        }
    }

    public static void updateRepliedList(Comment comment) throws DataBaseErrorException {
        String sql = "UPDATE Comment SET repliedComments = ? WHERE id = ?";
        Connection con = DataBaseConnector.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, DataHelpers.stringify(comment.getRepliedCommentsIds()));
            stmt.setInt(2, comment.getId());
            stmt.executeUpdate();
            stmt.close();
            DataBaseConnector.releaseConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
            DataBaseConnector.releaseConnection(con);
            throw new DataBaseErrorException();
        }
    }

    private static void commentDomainToDB(Comment comment, PreparedStatement st) throws SQLException {
        st.setInt(1, comment.getId());
        st.setString(2, comment.getContainingText());
        if (comment.isReply())
            st.setInt(3, 1);
        else
            st.setInt(3, 0);
        st.setInt(4, comment.getCommentedPollId());
        st.setInt(5, comment.getRepliedCommentId());
        st.setInt(6, comment.getCommenterId());
        st.setString(7, DataHelpers.stringify(comment.getRepliedCommentsIds()));
    }

    private static Comment commentDBtoDomain(ResultSet rs) throws SQLException, DataBaseErrorException {
        Comment comment = new Comment();
        comment.setId(rs.getInt("id"));
        comment.setContainingText(rs.getString("containingText"));
        if (rs.getInt("isReply") == 1)
            comment.setReply(true);
        else
            comment.setReply(false);
        comment.setCommentedPollId(rs.getInt("commentedPollId"));
        comment.setRepliedCommentId(rs.getInt("repliedCommentId"));
        comment.setCommenterId(rs.getInt("commenterId"));

        for (int cmId : DataHelpers.makeList(rs.getString("repliedComments")))
            comment.addRepliedComment(getComment(cmId));

        return comment;
    }
}

//"(id, containingText, isReply, commentedPollId, repliedCommentId, commenterId)"