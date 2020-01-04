package BusinessLogic;

import DataManagers.CommentDataHandler;
import DataManagers.PollDataHandler;
import ErrorClasses.*;
import Models.Comment;
import Models.Poll;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommentServices {
    public static void deleteComment(int userId, JSONObject data) throws JSONException, DataBaseErrorException, ObjectNotFoundInDBException, NoCommentWithThisId, UserWasNotInvitedException, NotTheOwnerException {
        int commentId = data.getInt("commentId");
        int pollId = data.getInt("pollId");
        Poll poll = PollServices.getPoll(pollId);

        checkPreConstraintsForDeleteComment(userId, commentId, poll);

        Comment comment = CommentDataHandler.getComment(commentId);

        if (poll.getOwnerId() != userId && comment.getCommenterId() != userId)
            throw new NotTheOwnerException();

        if(comment.isReply()){
            int repliedCommentId = comment.getRepliedCommentId();
            Comment repliedComment = CommentDataHandler.getComment(repliedCommentId);
            repliedComment.removeRepliedComment(comment.getId());
            CommentDataHandler.updateRepliedList(repliedComment);
        }
        else{
            poll.deleteComment(commentId);
            PollDataHandler.updateComments(poll);
        }

        deleteRepliedComments(poll, comment);

        poll.removeCommentId(commentId);
        PollDataHandler.updateCommentIds(poll);
        CommentDataHandler.removeComment(commentId);
    }

    public static void deleteRepliedComments(Poll poll, Comment comment) throws DataBaseErrorException {
        ArrayList<Integer> replies = comment.getRepliedCommentsIds();
        while (replies.size() > 0){
            int replyOfCommentId = replies.get(0);
            Comment replyComment = CommentDataHandler.getComment(replyOfCommentId);
            replies.addAll(replyComment.getRepliedCommentsIds());
            poll.removeCommentId(replyOfCommentId);
            CommentDataHandler.removeComment(replyOfCommentId);
            replies.remove(0);
        }
    }

    public static void checkPreConstraintsForDeleteComment(int userId, int commentId, Poll poll) throws ObjectNotFoundInDBException, UserWasNotInvitedException, NoCommentWithThisId {
        if(poll == null)
            throw new ObjectNotFoundInDBException();
        if(!poll.isUserInvited(userId))
            throw new UserWasNotInvitedException();
        if(!poll.doesContainComment(commentId))
            throw new NoCommentWithThisId();
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

        Comment newComment = createComment(userId, data, isReply, repliedCommentId, poll);

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

    public static Comment createComment(int userId, JSONObject data, boolean isReply, int repliedCommentId, Poll poll) throws DataBaseErrorException, JSONException {
        Comment newComment = new Comment();
        newComment.setCommenterId(userId);
        newComment.setCommenterName(UserServices.getUserName(userId));
        newComment.setRepliedCommentId(repliedCommentId);
        newComment.setCommentedPollId(poll.getId());
        newComment.setReply(isReply);
        newComment.setContainingText(data.getString("text"));
        return newComment;
    }

    public static void editComment(int userId, JSONObject data) throws JSONException, ObjectNotFoundInDBException, UserWasNotInvitedException, DataBaseErrorException, NoCommentWithThisId, AccessViolationException {
        int pollId = data.getInt("pollId");
        int commentId = data.getInt("commentId");

        Poll poll = PollServices.getPoll(pollId);
        if (poll == null)
            throw new ObjectNotFoundInDBException();
        if (!poll.isUserInvited(userId))
            throw new UserWasNotInvitedException();

        Comment comment = CommentDataHandler.getComment(commentId);
        if (comment.getCommentedPollId() != poll.getId())
            throw new NoCommentWithThisId();
        if (comment.getCommenterId() != userId)
            throw new AccessViolationException();

        comment.setContainingText(data.getString("text"));
        CommentDataHandler.updateCommentText(comment);
    }
}
