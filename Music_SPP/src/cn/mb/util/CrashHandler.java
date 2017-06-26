package cn.mb.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import cn.mb.app.AppAplication;

public class CrashHandler implements UncaughtExceptionHandler {

	private static CrashHandler instance;

	private CrashHandler() {
	}

	public synchronized static CrashHandler getInstance() { // 同步方法，以免单例多线程环境下出现异常
		if (instance == null) {
			instance = new CrashHandler();
		}
		return instance;
	}


	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				
//			}
//		}).start();

		String crashReport =getCrashReport(AppAplication.getApplication(),ex);
//		sendAppCrashReport(AiduApplication.getApplication(), crashReport);

		saveErrorLog(crashReport);
	}
	private String getCrashReport(Context context, Throwable ex) {
		try{
		PackageManager manager = context.getPackageManager();
		PackageInfo pinfo = manager.getPackageInfo(context.getPackageName(), 0);
		StringBuffer exceptionStr = new StringBuffer();
		exceptionStr.append("Version: "+pinfo.versionName+"("+pinfo.versionCode+")\n");
		exceptionStr.append("Android: "+android.os.Build.VERSION.RELEASE+"("+android.os.Build.MODEL+")\n");
		exceptionStr.append("Exception: "+ex.getMessage()+"\n");
		StackTraceElement[] elements = ex.getStackTrace();
		for (int i = 0; i < elements.length; i++) {
			exceptionStr.append(elements[i].toString()+"\n");
		}
		return exceptionStr.toString();
		}catch (Exception e) {
			return e.getMessage();
		}
	}

	/**
	 * 保存异常日志
	 * @param log
	 */
	public void saveErrorLog(String log) {
		String errorlog = "errorlog.txt";
		String savePath = "";
		String logFilePath = "";
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			//判断是否挂载了SD卡
			String storageState = Environment.getExternalStorageState();
			if(storageState.equals(Environment.MEDIA_MOUNTED)){
				savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/aidu/log/";
				File file = new File(savePath);
				if(!file.exists()){
					file.mkdirs();
				}
				logFilePath = savePath + errorlog;
			}
			//没有挂载SD卡，无法写文件
			if(logFilePath == ""){
				return;
			}
			File logFile = new File(logFilePath);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			fw = new FileWriter(logFile,true);
			pw = new PrintWriter(fw);
			pw.println("--------------------"+(new Date().toLocaleString())+"---------------------");
			//excp.printStackTrace(pw);
			pw.println(log);
			pw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(pw != null){ pw.close(); }
			if(fw != null){ try { fw.close(); } catch (IOException e) { }}
		}

	}
}
