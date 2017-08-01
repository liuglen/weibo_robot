package weibo4j.examples.comment;

import weibo4j.Comments;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.Comment;
import weibo4j.model.WeiboException;

public class CreateComment {

	public static void main(String[] args) {
		String access_token = "2.00e6JrLGUFJn_Ce98f8c1094fIN44E";
		String comments = "test";
		String id = "4134226053902711";
		Comments cm = new Comments(access_token);
		try {
			Comment comment = cm.createComment(comments, id);
			Log.logInfo(comment.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
}
