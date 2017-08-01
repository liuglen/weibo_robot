package weibo4j.examples.timeline;

import java.util.Date;

import weibo4j.Timeline;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.Status;
import weibo4j.model.WeiboException;
import weibo4j.util.WeiboConfig;

public class UpdateStatus {

	/*public static void main(String[] args) {
		String access_token = args[0];
		String statuses = args[1];
		Timeline tm = new Timeline(access_token);
		try {
			Status status = tm.updateStatus(statuses);
			Log.logInfo(status.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}	
	}*/

	@SuppressWarnings("finally")
	public static void main(String[] args) throws WeiboException, InterruptedException {
		String access_token = "2.00e6JrLGUFJn_Ce98f8c1094fIN44E";
		Date date = new Date();
		String statuses = date + "test weibo! https://www.baidu.com ";
		while(true){
			Timeline tm = new Timeline(access_token);
			Status status = new Status();
			try {
				status = tm.share(statuses);
				Log.logInfo(status.toString());
			} catch (WeiboException e) {
				e.printStackTrace();
			}
			if(status.getUser()!= null)
				break;
			Thread.sleep(1000);
		}
	}
}
