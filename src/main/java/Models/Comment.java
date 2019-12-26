package Models;

import java.util.ArrayList;

public class Comment {
    private static int count = 0;
    private int id;
    private String containingText;
    private boolean isReply;
    private int commentedPollId;
    private int repliedCommentId = -1;
    private int commenterId;
    private ArrayList<Comment> repliedComments = new ArrayList<>();

    public Comment() {id = count; count += 1;}


    public ArrayList<Comment> getRepliedComments() {
        return repliedComments;
    }

    public void setRepliedComments(ArrayList<Comment> repliedComments) {
        this.repliedComments = repliedComments;
    }

    public void addRepliedComment(Comment comment) {
        repliedComments.add(comment);
    }

    public ArrayList<Integer> getRepliedCommentsIds() {
        ArrayList<Integer> retList = new ArrayList<>();
        for (Comment comment : repliedComments)
            retList.add(comment.getId());
        return retList;
    }

    public int getCommentedPollId() {
        return commentedPollId;
    }

    public void setCommentedPollId(int repliedPollId) {
        this.commentedPollId = repliedPollId;
    }

    public int getCommenterId() {
        return commenterId;
    }

    public void setCommenterId(int commenterId) {
        this.commenterId = commenterId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContainingText() {
        return containingText;
    }

    public void setContainingText(String text) {
        this.containingText = text;
    }

    public boolean isReply() {
        return isReply;
    }

    public void setReply(boolean reply) {
        isReply = reply;
    }

    public int getRepliedCommentId() {
        return repliedCommentId;
    }

    public void setRepliedCommentId(int repliedCommentId) {
        this.repliedCommentId = repliedCommentId;
    }
}
