package cn.mb.app;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.widget.ImageView;
import cn.mb.hk.music.R;
import cn.mb.model.entity.DownloadVideoInfo;
import cn.mb.util.CrashHandler;
import cn.mb.util.FileUtils;
import cn.sixpower.spp.SppAppApplication;

public class AppAplication extends SppAppApplication {
    public static ExecutorService pool = Executors.newFixedThreadPool(6);

    public static final int PAGE_SIZE = 10;// 默认分页大小

    public static volatile boolean tag = false;// 是否處於運行中的app

    private Hashtable<String, Object> memCacheRegion = new Hashtable<String, Object>();

    public static DbUtils db;

    private static BitmapUtils bitmapUtils;

    private static AppAplication application;

    public static volatile int ALL_PM = 0;
    public static volatile int ALL_ZB = 0;
    public static volatile int ALL_LIFE_TIME = 0;
    public static  int colorRGB = -1;//volatile
    public static  int colorRGBProcess = 100;//volatile
    
    public static ProductType productType;
    public static String SCAN_DEVICE_NAME = "PZ";
    /**
     * OPlayer SD卡缓存路径
     */
    public static final String OPLAYER_CACHE_BASE = Environment.getExternalStorageDirectory() + "/zjlayer";
    /**
     * 视频截图缓冲路径
     */
    public static final String OPLAYER_VIDEO_THUMB = OPLAYER_CACHE_BASE + "/thumb/";
    /**
     * 首次扫描
     */
    public static final String PREF_KEY_FIRST = "application_first";

//    public static VideoDownloadManager downloadManager;

    /**
     * 保存下载对象，key-softUrl, val-下载实体
     */
    public static Map<String, DownloadVideoInfo> downloadInfos = new ConcurrentHashMap<String, DownloadVideoInfo>();

    public  static volatile int bgResId=AppConstant.bgDataList[0];
    public  static volatile int currentBannerResId=AppConstant.bannerDarkDataList[0];
    
    public static Application getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        initDb();

        Thread.setDefaultUncaughtExceptionHandler(CrashHandler.getInstance());

        init();
    }

    private void init() {
        // 创建缓存目录
        FileUtils.createIfNoExists(OPLAYER_CACHE_BASE);
        FileUtils.createIfNoExists(OPLAYER_VIDEO_THUMB);
    }

    /**
     * 从内存缓存中获取对象
     *
     * @param key
     * @return
     */
    public Object getMemCache(String key) {
        return memCacheRegion.get(key);
    }

    public void setMemCache(String key, Object o) {
        memCacheRegion.put(key, o);
    }

    /**
     * 保存磁盘缓存
     *
     * @param key
     * @param value
     * @throws IOException
     */
    public void setDiskCache(String key, String value) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput("cache_" + key + ".data", Context.MODE_PRIVATE);
            fos.write(value.getBytes());
            fos.flush();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 获取磁盘缓存数据
     *
     * @param key
     * @return
     * @throws IOException
     */
    public String getDiskCache(String key) throws IOException {
        FileInputStream fis = null;
        try {
            fis = openFileInput("cache_" + key + ".data");
            byte[] datas = new byte[fis.available()];
            fis.read(datas);
            return new String(datas);
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
    }

    // display img
    public static void displayImg(String url, ImageView iv) {
        getBitmapUtils().display(iv, url);
    }

    public static BitmapUtils getBitmapUtils() {
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(getApplication(), FileUtils.getPath(AppConstant.CACHE_IMG));
            bitmapUtils.configDefaultLoadingImage(R.drawable.pv_video_default);
            bitmapUtils.configDefaultLoadFailedImage(R.drawable.pv_video_default);
            /*
			 * bitmapUtils.configDiskCacheFileNameGenerator(new
			 * FileNameGenerator() {
			 * 
			 * @Override public String generate(String arg0) {
			 * 
			 * return new Md5FileNameGenerator().generate(arg0); } });
			 */
        }
        return bitmapUtils;
    }

    public static List<Activity> listActivity = new ArrayList<Activity>();

   /* public void exitSystem() {
        for (Activity item : listActivity) {
            item.finish();
        }
        // this.userInfo=new UserInfo();
        // Config.getAppConfig(this).remove("user.appKey","user.userName","user.cellPhone","user.userId");
//        android.os.Process.killProcess(android.os.Process.myPid());
        
        System.exit(0);
    }*/

    private void initDb() {
        db = DbUtils.create(this, AppConstant.DB_NAME);
        db.configAllowTransaction(true);
        db.configDebug(false);
    }
    
    @Override
    public void onTerminate() {
    	super.onTerminate();
    	  for (Activity item : listActivity) {
              item.finish();
          }
          // this.userInfo=new UserInfo();
          // Config.getAppConfig(this).remove("user.appKey","user.userName","user.cellPhone","user.userId");
//          android.os.Process.killProcess(android.os.Process.myPid());
          
          System.exit(0);
          android.os.Process.killProcess(android.os.Process.myPid());
    }
    
   
    
}
