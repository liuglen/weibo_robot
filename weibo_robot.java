package weibo_robot;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

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
	
	private static String access_token = "";
	
	public weibo_robot(String accessToken){
		access_token = accessToken;
	}
	
	public static void main(String [] args) throws Exception{
		weibo_robot wr = new weibo_robot("2.00tIMonCUFJn_C03cd3920cfbWJ5FB");
		Storage s = wr.new Storage();
		ExecutorService service = Executors.newCachedThreadPool();
		
		putWeibo pw = wr.new putWeibo(s);
		getWeibo gw = wr.new getWeibo(s);
		
		
		service.execute(gw);
		service.execute(pw);
	}
	/**
	 *  从仓库中取出一条微博进行评论 转发或发送
	 * @author 刘
	 *
	 */
	public class putWeibo implements Runnable{
		
		private Storage s = null;
		
		public putWeibo(Storage s){
			this.s = s;
		}

		public void run(){
			try{
				while(true){
					System.out.println("putweibo run");
					Thread.sleep(1000);
					Status temp = s.pop();
					comment_weibo(temp.getText(),temp.getId());
					Date date = new Date();
					System.out.println(date + "成功评论一条微博");
					Thread.sleep(60*1000);
				}
			}catch(InterruptedException e){
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
				/**
		 *下载图片到本地
		 *@param picurl 图片的下载地址
		 */
		public void downloadPic(String picurl) throws Exception {
			// 构造URL
		    URL url = new URL(picurl);
		    // 打开连接
		    URLConnection con = url.openConnection();
		    // 输入流
		    InputStream is = con.getInputStream();
		    // 1K的数据缓冲
		    byte[] bs = new byte[1024];
		    // 读取到的数据长度
		    int len;
		    // 输出的文件流
		    long  fileName = System.currentTimeMillis();
		    FileOutputStream os = new FileOutputStream(new File("D://weibo_image//"+fileName+".jpg"));
		    // 开始读取
		    while ((len = is.read(bs)) != -1) {
		      os.write(bs, 0, len);
		    }
		    // 完毕，关闭所有链接
		    os.close();
		    is.close();
	    }
		/**
		 * 微博点开全文
		 * @param wholeurl:全文的链接
		 * 
		 * */
		public String wholeText(String url) throws Exception{
			Runtime rt = Runtime.getRuntime();
	        Process process = null;
	        try {
	            process = rt.exec("I:/java/weibo4j-oauth2/phantomjs.exe I:/java/weibo4j-oauth2/parser.js " +url);
	            InputStream in = process.getInputStream();
	            InputStreamReader reader = new InputStreamReader(in, "UTF-8");
	            BufferedReader br = new BufferedReader(reader);
	            StringBuffer sbf = new StringBuffer();
	            String tmp = "";
	            while ((tmp = br.readLine()) != null) {
	                sbf.append(tmp);
	            }
	            String result = sbf.toString().replaceAll("<[^>]*>", "");
	            System.out.println(result);
	            return result;
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null;
	        
		}
		/**
		 * 转发微博
		 * @param text
		 */
		public void repost_weibo(String text){
			
		}
		/**
		 * 评论微博
		 * @param text
		 * @param strid
		 * @throws Exception
		 */
		public void comment_weibo(String text, String strid) throws Exception{
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
		 * @throws UnsupportedEncodingException 
		 */
		public void share_weibo(String text) throws WeiboException, InterruptedException, UnsupportedEncodingException {
			Date date = new Date();
			String statuses = date + text + " https://www.baidu.com ";
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
		public void share_weibo(String text, String PicName) throws WeiboException, InterruptedException {
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
		 * 读取图片文件
		 * @param filename
		 * @return
		 * @throws IOException
		 */
		@SuppressWarnings("resource")
		public byte[] readFileImage(String filename) throws IOException {
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
	 * 每隔十分钟读取一次微博
	 * @author 刘
	 *
	 */
	 public class getWeibo implements Runnable{
		 	private long sid = 4138039293022421L;
			private long mid = 4138039293022421L;
			private long num = 0L;
			private Storage s = null;
			private List<Status> res = null;
			
			public getWeibo(Storage s){
				this.s = s;
			}

			public void run() {
				try{
					while(true){
						System.out.println("getweibo run");
						Date date = new Date();
						read_weibo();
						System.out.println("ok");
						System.out.println(date + "成功读取"+num+"条微博");
						Thread.sleep(10*60*1000);
					}
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			public void read_weibo() throws InterruptedException{
				final Timeline tm = new Timeline(access_token);	            	
		        		
				//第一次查询：获得最新的一条微博的ID
				Paging p = new Paging(1,1);
        		try {
        			StatusWapper status = tm.getHomeTimeline(0,0,p);
        			Log.logInfo(status.toString());
        			sid = mid;
        			mid = Long.parseLong(status.getStatuses().get(0).getId());//获得最新的微博ID
        		} catch (WeiboException e) {
        			e.printStackTrace();
        		}
        		//System.out.println("1 end!");
        		
        		//第二次查询：真正的查询，查询一段时间内的
        		p = new Paging(1,100,sid,mid);//这里！对page和count的理解！
        		try {
        			StatusWapper status = tm.getHomeTimeline(0,0,p);
        			res = status.getStatuses();
        			System.out.println("***************"+res.size());
        			Log.logInfo(status.toString());
        		} catch (WeiboException e) {
        			e.printStackTrace();
        		}
        		//System.out.println("2 end!");
        		for(int i = 0; i < res.size(); ++i)
    				s.push(res.get(i));
            }
						
	 }
	 /**
	  * 仓库 存储微博
	  * @author 刘
	  *
	  */
	 public class Storage{
		 BlockingQueue<Status> queue = new LinkedBlockingQueue<Status>();
		 
		 public void push(Status status) throws InterruptedException{
			 queue.put(status);
		 }
		 
		 public Status pop() throws InterruptedException{
			 return queue.take();
		 }
	 }
}

