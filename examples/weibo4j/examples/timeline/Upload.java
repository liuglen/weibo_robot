package weibo4j.examples.timeline;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import weibo4j.Timeline;
import weibo4j.http.ImageItem;
import weibo4j.model.Status;
import weibo4j.util.WeiboConfig;

public class Upload {
	public static void main(String args[]) {
		try {
			try {
				Date date = new Date(); 
				byte[] content = readFileImage("test.jpg");
				System.out.println("content length:" + content.length);
				ImageItem pic = new ImageItem("pic", content);
				String s = java.net.URLEncoder.encode(date+" test ", "utf-8") + "http://www.baidu.com";
				String access_token = "2.00e6JrLGUFJn_Ce98f8c1094fIN44E";
				Timeline tm = new Timeline(access_token);
				Status status = tm.share(s, pic);

				System.out.println("Successfully upload the status to ["
						+ status.getText() + "].");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception ioe) {
			System.out.println("Failed to read the system input.");
		}
	}

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
