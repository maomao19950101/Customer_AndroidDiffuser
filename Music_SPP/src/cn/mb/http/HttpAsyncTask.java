package cn.mb.http;

import java.io.File;
import java.io.UnsupportedEncodingException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import cn.mb.util.FileUtils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;


/**
 * 网络请求工具类
 *
 */
public class HttpAsyncTask {
	public static final int STATUS_SUCCESS = 1;

	private static ProgressDialog alertDialog; 
	
	public static HttpCallBack callBack;
	
	private  static HttpAsyncTask instance;

	public static HttpAsyncTask getInstance() {
		if (instance == null) {
			synchronized (HttpAsyncTask.class) {
				if (instance == null) {
					instance = new HttpAsyncTask();
				}
			}
		}
		return instance;
	}
	
	protected HttpAsyncTask(){}
	
	public static void setHttpCallBack(HttpCallBack mCallBack){
		if(mCallBack!=null)
			 callBack=mCallBack;
	}
//	
	public  interface  HttpCallBack{
		
		/**
		 * 数据请求成功
		 * @param <T>
		 */
		public <T> void onSuccessCallBack(int reqCode,ResponseInfo<T> result);
		/**
		 * 数据请求开始
		 */
		public void onStartCallBack(int reqCode);
		/**
		 * 数据请求失败
		 * @param error
		 * @param msg
		 */
		public void onFailureCallBack(int reqCode,HttpException error, String msg) ;
		/**
		 * 数据请求停止
		 */
		public void onCancelledCallBack(int reqCode);
		/**
		 * 数据请求中
		 * @param total
		 * @param current
		 * @param isUploading
		 */
		public void onLoadingCallBack(int reqCode,long total, long current, boolean isUploading);
		
		/**
		 * 缓存数据读取后
		 * @param reqCode
		 * @param content
		 */
	    public void onReadCacheCallBack(int reqCode,String content);
		
	}
	
	public static HttpUtils getHttpUtils(){
	        return new HttpUtils();
	   }
	

	
	/**
	 * 读取缓存
	 * @param context
	 * @param reqCode
	 * @param fileName
	 */
	public static void readCache(final Context context,final int reqCode,final String fileName,final HttpCallBack httpCallBack){
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
					StringBuilder content = null;
					try {
						content = FileUtils.readFile(context.getExternalCacheDir().getPath()+"/files/"+fileName+".txt", "UTF-8");
					} catch (Exception e) {
						e.printStackTrace();
					}
					return content==null ? null : content.toString();
		
			}
			protected void onPostExecute(String content) {
				if(httpCallBack!=null)
					httpCallBack.onReadCacheCallBack(reqCode, content);
			};
			
		}.execute();
	}
	
	/**
	 * 网络数据缓存文件保存至/mnt/sdcard/Android/data/com.yeamo4/files
	 * @param context
	 * @param reqCode
	 * @param params
	 * @param mUrl
	 * @param isWithCache
	 * @param httpCallBack
	 */
	public static void basePost(final Context context,final int reqCode, RequestParams params,final String mUrl,final boolean isWithCache,final String fileName,final HttpCallBack httpCallBack,String tip){
//		if(httpCallBack!=null){
//			callBack=httpCallBack;
//		}
		if(alertDialog!=null&&alertDialog.isShowing())alertDialog.dismiss();
		
		alertDialog = PromptManager.getProgressDialog(context, "",tip);
		
		readCache(context, reqCode, fileName,httpCallBack);
        
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST,mUrl, params,new RequestCallBack<String>() {
	        @Override
	        public void onStart() {
	        	if(httpCallBack!=null)
	        		httpCallBack.onStartCallBack(reqCode);
	        
	        }
	        @Override
	        public void onLoading(long total, long current, boolean isUploading) {
	        	if(httpCallBack!=null)
	        		httpCallBack.onLoadingCallBack(reqCode,total, current, isUploading);
	        
	        }
	        @Override
	        public void onSuccess(ResponseInfo<String> responseInfo) {	        	
               
              /* if(isWithCache&&StorageUtils.isExternalStorageWritable()&&StringUtils.isNotEmpty(fileName)){
            		String savePath=context.getExternalCacheDir().getPath()+"/files";
            		FileUtils.createFileByContent(savePath, fileName+".txt", responseInfo.result.toString(), false);
            	}*/
               
              if(httpCallBack!=null){
            	  httpCallBack.onSuccessCallBack(reqCode,responseInfo);
              }
          	d();   
	        }
	        @Override
	        public void onFailure(HttpException error, String msg) {
	        	if(httpCallBack!=null)
	        		httpCallBack.onFailureCallBack(reqCode,error, msg);
	        	d();
	        }
	        @Override
	        public void onCancelled() {
	        	if(httpCallBack!=null)
	        		httpCallBack.onCancelledCallBack(reqCode);
	        	d();
	        }
	});
	}
	/**
	 * 网络请求数据
	 * @param context
	 * @param reqCode
	 * @param params
	 * @param mUrl
	 * @param appendKey
	 * @param isCache
	 * @param httpCallBack
	 */
	public synchronized static void post(Context context,int reqCode,RequestParams params,String mUrl,boolean isCache,String fileName,HttpCallBack httpCallBack,String tip){
		if(params==null){
			 params = new RequestParams();
		}
		
		basePost(context, reqCode, params, mUrl,isCache,fileName, httpCallBack,tip);
	}
	/**
	 * 文件上传
	 * @param context
	 * @param reqCode
	 * @param file
	 * @param params
	 * @param mUrl
	 * @param appendKey
	 * @param httpCallBack
	 * @throws UnsupportedEncodingException
	 */
	public static void fileUpload(Context context,int reqCode,File file,RequestParams params,String mUrl,String appendKey,HttpCallBack httpCallBack) throws UnsupportedEncodingException{
		if(params==null){
			 params = new RequestParams();
		}
		
		basePost(context, reqCode, params, mUrl,false,null, httpCallBack,"");
	}
	
	/**
	 * 文件下载，支持断点下载
	 * @param <T>
	 * @param context
	 * @param reqCode
	 * @param url
	 * @param target
	 * @param autoResume
	 * @param autoRename
	 * @param httpCallBack
	 * @param 暂停下载  httpHandler.cancel()
	 */
	public static  HttpHandler<File> fileDownload(final Context context,final int reqCode,String url, String target, boolean autoResume, boolean autoRename, final HttpCallBack httpCallBack){
//		if(httpCallBack!=null){
//			callBack=httpCallBack;
//		}
		
		HttpUtils http = new HttpUtils();
		HttpHandler<File> httpHandler = http.download(url,target,
				autoResume, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
				autoRename, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
				new RequestCallBack<File>() {
					@Override
					public void onStart() {
                       if(httpCallBack!=null)
                    	   httpCallBack.onStartCallBack(reqCode);
					}

					@Override
					public void onLoading(long total, long current,boolean isUploading) {
						 if(httpCallBack!=null)
							 httpCallBack.onLoadingCallBack(reqCode, total, current, isUploading);
					}

					@Override
					public void onSuccess(ResponseInfo<File> responseInfo) {
			              if(httpCallBack!=null)
			            	  httpCallBack.onSuccessCallBack(reqCode,responseInfo);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						 if(httpCallBack!=null)
							 httpCallBack.onFailureCallBack(reqCode, error, msg);
					}
					@Override
					public void onCancelled() {
						 if(httpCallBack!=null)
							 httpCallBack.onCancelledCallBack(reqCode);
					}
				});
		
		
		return  httpHandler;
	
	}
	
	
	private static void d(){
		if(alertDialog!=null&&alertDialog.isShowing())alertDialog.dismiss();
	}
}
