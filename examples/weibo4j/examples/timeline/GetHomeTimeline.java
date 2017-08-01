package weibo4j.examples.timeline;

import weibo4j.Timeline;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.FriendsTimelineIds;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;

public class GetHomeTimeline {

	public static void main(String[] args) {
		String access_token = "2.00e6C3YFUFJn_C6aaaad7a48aJvTJB";
		Timeline tm = new Timeline(access_token);
		try {
			FriendsTimelineIds status = tm.getFriendsTimelineIds();
			Log.logInfo(status.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}

}
