package weibo4j.examples.timeline;

import weibo4j.Timeline;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.Status;
import weibo4j.model.WeiboException;

public class Repost {

	public static void main(String[] args) {
		String access_token = "2.00e6JrLGUFJn_Ce98f8c1094fIN44E";
		String id = "4135561390213050";
		Timeline tm = new Timeline(access_token);
		try {
			Status status = tm.repost(id);
			Log.logInfo(status.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}

}
