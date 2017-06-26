package com.simple.player.util;

public class Constant {
	public static final String FLAG = "flag";
	public static final String POSITION = "position";
	public static final String GROUP = "group";
	public static final String ALL = "all";
	public static final String ALBUM = "album";
	public static final String ARTIST = "artist";
	
	public static final String UPDATADIAPALY = "updataDiaplay";	
	public static final String SEEKBAR = "seekbar";
	public static final String PLOPA = "plopa";
	public static final String NEXT = "next";
	public static final String PREVIOUS = "previous";
	
	public static final int CURRENT_TIME = 1;
	public static final int PAUSE = 10;
	public static final int PLAY = 20;
	public static final int IDLE = 30;
	public static int currentState = IDLE;
	
	public static String currentArr=Constant.ALL;
	public static int groupid = 0;
	public static int currentIndex;
	
	
	public static String title;
	public static String artist;
	public static String total="00:00";
	public static int duration;
	public static String _data;
	
	public static int playModler = Constant.order;
	public static final int loop = 40;
	public static final int order = 50;
	public static final int random = 60;
	
	public static final String ACTION = "com.mrzhu.mediaplay.receiver";
	public static final String CURR = "curr";
	public static final String MAXCURR = "maxcurr";
	public static final String UPDATACTION = "com.mrzhu.mediaplay.updata";
	public static int seekBarCurr;
	
	public static int interval = 25;
	public static int sizeWord = 25; //显示歌词文字的大小值
}
