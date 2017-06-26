package cn.mb.app;

public class RequestUrls {
	public static String DOMAIN = "http://47.89.181.108";
//	public static String DOMAIN = "http://112.74.12.111";
//	public static String DOMAIN = "http://47.88.104.235";
//	public static String DOMAIN = "http://bluebox.markbright.hk:8002/";
	
	public static final String GET_VIDEO_LIST=DOMAIN+"/Handler/Ashx_Video.ashx?action=VideoList";
	public static final String GET_PZ02_VIDEO_LIST=DOMAIN+"/Handler/Ashx_Video.ashx?action=VideoList";
	public static final String GET_RECIPE_LIST=DOMAIN+"/Handler/Ashx_SanBeng.ashx?action=Search";
	public static final String GET_VIDEO_CLASSIFY_LIST=DOMAIN+"/Handler/Ashx_Video.ashx?action=videoCategory";
//	public static final String GET_VIDEO_LIST=DOMAIN+"/Handler/Ashx_Video.ashx?action=VideoList";
}
