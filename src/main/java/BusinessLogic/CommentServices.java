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

public class CommentServices {

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
            if (!poll.doesContaintComment(repliedCommentId))
                throw new NoCommentWithThisId();
        }

        Comment newComment = new Comment();
        newComment.setCommenterId(userId);
        newComment.setRepliedCommentId(repliedCommentId);
        newComment.setCommentedPollId(poll.getId());
        newComment.setReply(isReply);
        newComment.setContainingText(data.getString("text"));

        poll.addComment(newComment);
        CommentDataHandler.addComment(newComment);
        PollDataHandler.updateComments(poll);
    }
}
