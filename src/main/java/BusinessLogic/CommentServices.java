package BusinessLogic;

import DataManagers.CommentDataHandler;
import DataManagers.PollDataHandler;
import ErrorClasses.DataBaseErrorException;
import ErrorClasses.NoCommentWithThisId;
import ErrorClasses.ObjectNotFoundInDBException;
import ErrorClasses.UserWasNotInvitedException;
import Models.Comment;
import Models.Poll;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommentServices {
    public static void deleteComment(JSONObject data) throws JSONException, DataBaseErrorException, ObjectNotFoundInDBException, NoCommentWithThisId {
        int commentId = data.getInt("commentId");
        int pollId = data.getInt("pollId");
        Poll poll = PollServices.getPoll(pollId);
        if(poll == null)
            throw new ObjectNotFoundInDBException();
        if(poll.doesContainComment(commentId) == false)
            throw new NoCommentWithThisId();

        Comment comment = CommentDataHandler.getComment(commentId);
        if(comment.isReply()){
            int repliedCommentId = comment.getRepliedCommentId();
            Comment repliedComment = CommentDataHandler.getComment(repliedCommentId);
            repliedComment.removeRepliedComment(comment);
            CommentDataHandler.updateRepliedList(repliedComment);
        }
        else{
            poll.deleteComment(commentId);
            PollDataHandler.updateComments(poll);
        }

        ArrayList<Integer> replies = comment.getRepliedCommentsIds();
        if(replies.size() > 0) {
            for (int replyOfCommentId : replies) {
                poll.removeCommentId(replyOfCommentId);
                CommentDataHandler.removeComment(replyOfCommentId);
            }
        }

        poll.removeCommentId(commentId);
        PollDataHandler.updateComments(poll);
        PollDataHandler.updateCommentIds(poll);
        CommentDataHandler.removeComment(commentId);
    }

    public static void addComment(int userId, JSONObject data) throws JSONException, ObjectNotFoundInDBException, DataBaseErrorException, UserWasNotInvitedException, NoCommentWithThisId {
        int pollId = data.getInt("pollId");
        boolean isReply = false;
        int repliedCommentId = -1;
        if (!data.isNull("repliedCommentId")) {
            isReply = true;
            repliedCommentId = data.getInt("repliedCommentId");
        }

        Poll poll = PollServices.getPoll(pollId);
        if (poll == null)
            throw new ObjectNotFoundInDBException();
        if (!poll.isUserInvited(userId))
            throw new UserWasNotInvitedException();
        if(isReply) {
            if (!poll.doesContainComment(repliedCommentId))
                throw new NoCommentWithThisId();
        }

        Comment newComment = new Comment();
        newComment.setCommenterId(userId);
        newComment.setCommenterName(UserServices.getUserName(userId));
        newComment.setRepliedCommentId(repliedCommentId);
        newComment.setCommentedPollId(poll.getId());
        newComment.setReply(isReply);
        newComment.setContainingText(data.getString("text"));

        CommentDataHandler.addComment(newComment);
        if (isReply) {
            Comment repliedTo = CommentDataHandler.getComment(repliedCommentId);
            repliedTo.addRepliedComment(newComment);
            CommentDataHandler.updateRepliedList(repliedTo);
        }
        else {
            poll.addComment(newComment);
            PollDataHandler.updateComments(poll);
        }
        poll.addCommentId(newComment.getId());
        PollDataHandler.updateCommentIds(poll);
    }
}
