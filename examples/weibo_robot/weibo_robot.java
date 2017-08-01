package weibo_robot;

import java.util.Date;

import weibo4j.Timeline;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.Status;
import weibo4j.model.WeiboException;

public class weibo_robot {
	
	private static  String access_token;
	
	public weibo_robot(String accessToken){
		access_token = accessToken;
	}
	
	public static void main(String [] args){
		
	}
	
	
	public static void repost_weibo(String text){
		
	}
	
	public static void read_latest_weibo()
	public static void update_weibo(String text) throws WeiboException, InterruptedException {
		//String access_token = "2.00e6JrLGUFJn_Ce98f8c1094fIN44E";
		//Date date = new Date();
		String statuses = text + "test weibo! https://www.baidu.com ";
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
