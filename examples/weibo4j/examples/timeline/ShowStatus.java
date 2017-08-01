package weibo4j.examples.timeline;

import weibo4j.Timeline;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.Status;
import weibo4j.model.WeiboException;

public class ShowStatus {

	public static void main(String[] args) {
		String access_token = "2.00e6C3YFUFJn_C6aaaad7a48aJvTJB";
		String id = "4136026072517497";
		Timeline tm = new Timeline(access_token);
		try {
			Status status = tm.showStatus(id);
			Log.logInfo(status.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}

}
