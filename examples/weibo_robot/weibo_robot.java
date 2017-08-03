package weibo_robot;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import weibo4j.Comments;
import weibo4j.Timeline;
import weibo4j.examples.oauth2.Log;
import weibo4j.http.ImageItem;
import weibo4j.model.Comment;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONObject;

public class weibo_robot {
	
	private static String access_token = "2.00e6C3YFUFJn_C6aaaad7a48aJvTJB";
	private static long lastest_id = 1;
	
	public weibo_robot(String accessToken){
		access_token = accessToken;
	}
	
	public static void main(String [] args) throws Exception{
		//List<Status> status = get_latest_weibo();
		//for(Status each : status){
		//	comment_weibo(each.getText(),each.getId());
		//}
		share_weibo("测试","test.jpg");
	}
	
	/**
	 * 转发微博
	 * @param text
	 */
	public static void repost_weibo(String text){
		
	}
	/**
	 * 获取最新微博
	 * @return
	 */
	public static List<Status> get_latest_weibo(){
		Timeline tm = new Timeline(access_token);
		try {
			//Paging paging = new Paging(1,100,lastest_id);
			StatusWapper status = tm.getFriendsTimeline();
			lastest_id = status.getStatuses().get(0).getIdstr() > 0 ? status.getStatuses().get(0).getIdstr(): lastest_id;
			return status.getStatuses();
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 评论微博
	 * @param text
	 * @param strid
	 * @throws Exception
	 */
	public static void comment_weibo(String text, String strid) throws Exception{
		JSONObject answer = new JSONObject();
		try{
			Timeline tm = new Timeline(access_token);
			answer = tm.tulingRobot(text);
		} catch (WeiboException e) {
			e.printStackTrace();
		}		
		Comments cm = new Comments(access_token);
		try {
			Comment comment = cm.createComment(answer.getString("text"), strid);
			Log.logInfo(comment.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 分享第三方链接到微博
	 * @param text
	 * @throws WeiboException
	 * @throws InterruptedException
	 */
	public static void share_weibo(String text) throws WeiboException, InterruptedException {
		//String access_token = "2.00e6JrLGUFJn_Ce98f8c1094fIN44E";
		//Date date = new Date();
		String statuses = text + "test weibo! https://www.baidu.com ";
		while(true){
			Timeline tm = new Timeline(access_token);
			Status status = new Status();
			try {
				status = tm.share(statuses);
				//Log.logInfo(status.toString());
			} catch (WeiboException e) {
				e.printStackTrace();
			}
			if(status.getUser()!= null)
				break;
			Thread.sleep(1000);
		}
	}
	/**
	 * 分享有图片的第三方链接到微博
	 * @param text
	 * @param PicName
	 * @throws WeiboException
	 * @throws InterruptedException
	 */
	public static void share_weibo(String text, String PicName) throws WeiboException, InterruptedException {
		//String access_token = "2.00e6JrLGUFJn_Ce98f8c1094fIN44E";
		while(true){
			Status status = new Status();
			try {
				try {
					Date date = new Date(); 
					byte[] content = readFileImage("test.jpg");
					System.out.println("content length:" + content.length);
					ImageItem pic = new ImageItem("pic", content);
					String s = java.net.URLEncoder.encode(date+ text, "utf-8") + "http://www.baidu.com";
					String access_token = "2.00e6JrLGUFJn_Ce98f8c1094fIN44E";
					Timeline tm = new Timeline(access_token);
					status = tm.share(s, pic);

					System.out.println("Successfully upload the status to ["
							+ status.getText() + "].");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} catch (Exception ioe) {
				System.out.println("Failed to read the system input.");
			}
			if(status.getUser()!= null)
				break;
			Thread.sleep(1000);
		}
	}
	/**
	 * 使用JSON发送POST请求
	 * @param Url
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static String httpPostWithJSON(String Url, JSONObject obj) throws Exception {
        try {
            URL url = new URL(Url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            byte[] data = (obj.toString()).getBytes();
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            conn.setRequestProperty("contentType", "application/json");
            conn.connect();
            OutputStream  out = conn.getOutputStream();     
            out.write((obj.toString()).getBytes());
            out.flush();
            out.close();
            System.out.println(conn.getResponseCode());
            if (conn.getResponseCode() == 200) {
                //System.out.println("连接成功");
                InputStream in = conn.getInputStream();
                String a = null;
                try {
                    byte[] data1 = new byte[in.available()];
                    in.read(data1);
                    a = new String(data1);
                    return a;
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } else {
                System.out.println("no++");
            }

        } catch (Exception e) {

        }
		return "";
    }
	/**
	 * 读取图片文件
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static byte[] readFileImage(String filename) throws IOException {
		BufferedInputStream bufferedInputStream = new BufferedInputStream(
				new FileInputStream(filename));
		int len = bufferedInputStream.available();
		byte[] bytes = new byte[len];
		int r = bufferedInputStream.read(bytes);
		if (len != r) {
			bytes = null;
			throw new IOException("读取文件不正确");
		}
		bufferedInputStream.close();
		return bytes;
	}
}
