package cn.mb.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import cn.mb.app.AppConstant;

public class SimpleHttpUtil {
	private List<SimpleHttpCallback> listCallBack= new ArrayList<SimpleHttpUtil.SimpleHttpCallback>();
	
	private static Header[] headers;

	static {
		headers = new BasicHeader[10];
		headers[0] = new BasicHeader("Appkey", "");
		headers[1] = new BasicHeader("Udid", "");// 手机串号
		headers[2] = new BasicHeader("Os", "android");//
		headers[3] = new BasicHeader("Osversion", "");//
		headers[4] = new BasicHeader("Appversion", "");// 1.0
		headers[5] = new BasicHeader("Sourceid", "");//
		headers[6] = new BasicHeader("Ver", "");
		headers[7] = new BasicHeader("Userid", "");
		headers[8] = new BasicHeader("Usersession", "");
		headers[9] = new BasicHeader("Unique", "");
	}
	public interface SimpleHttpCallback{
		void onResponseResult(int command,String resultStr);
	}
	// 创建HttpClient对象
	public static  HttpClient httpClient = new DefaultHttpClient();

	// private static ExecutorService pool = Executors.newFixedThreadPool(5);
	public synchronized String getRequest(String url) {
		HttpGet get = new HttpGet(url);
		HttpResponse response;
		HttpParams httpParams = new BasicHttpParams();//
		HttpConnectionParams.setConnectionTimeout(httpParams, 8000);
		HttpConnectionParams.setSoTimeout(httpParams, 8000);
		get.setParams(httpParams);
		String result = "{\"message\":\"网络异常\",\"status\":\"0\"}";
		try {
			response = httpClient.execute(get);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				result = EntityUtils.toString(response.getEntity(), AppConstant.ENCODING);
			} else {
				if (statusCode >= 500)
					result = "{\"message\":\"服务器内部错误" + statusCode + "\",\"status\":\"0\"}";
				if (statusCode < 500 && statusCode >= 400)
					result = "{\"message\":\"请求资源问题" + statusCode + "\",\"status\":\"0\"}";
				if (statusCode < 400 && statusCode >= 300)
					result = "{\"message\":\"网络代理问题" + statusCode + "\",\"status\":\"0\"}";
			}
			get.abort();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (get != null)
				get.abort();
		}
		System.out.println("--result: " + result);
		return result;
	}

	public static   synchronized String postRequest(String url, Map<String, String> params) {
		// 创建HttpPost对象。
		HttpPost post = new HttpPost(url);
		// post.setHeaders(headers);
		HttpResponse response;
		// 处理超时
		HttpParams httpParams = new BasicHttpParams();//
		httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 8000);
		HttpConnectionParams.setSoTimeout(httpParams, 8000);
		post.setParams(httpParams);
		String result = "{\"message\":\"网络异常\",\"status\":\"0\"}";
		try {
			// 设置参数
			if (params != null && params.size() > 0) {
				List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
				for (Map.Entry<String, String> item : params.entrySet()) {
					BasicNameValuePair pair = new BasicNameValuePair(item.getKey(), item.getValue());
					parameters.add(pair);
				}
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, AppConstant.ENCODING);
				post.setEntity(entity);
			}

			response = httpClient.execute(post);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				result = EntityUtils.toString(response.getEntity(), AppConstant.ENCODING);
			} else {
				if (statusCode >= 500)
					result = "{\"message\":\"服务器内部错误" + statusCode + "\",\"status\":\"0\"}";
				if (statusCode < 500 && statusCode >= 400)
					result = "{\"message\":\"请求资源问题" + statusCode + "\",\"status\":\"0\"}";
				if (statusCode < 400 && statusCode >= 300)
					result = "{\"message\":\"网络代理问题" + statusCode + "\",\"status\":\"0\"}";
			}
			post.abort();
		} catch (Exception e) {
			e.printStackTrace();
			// 网络异常
		} finally {
			if (post != null)
				post.abort();
		}
		System.out.println("--result: " + result);
		return result;
	}

	
	public  synchronized void postRequest(final String url,final Map<String, String> paramsMap,final int command,final String tip,final Context context) {
		new AsyncTask<Void, Void, String>(){
			private ProgressDialog alertDialog;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				if(alertDialog!=null&&alertDialog.isShowing())alertDialog.dismiss();
					alertDialog = PromptManager.getProgressDialog(context, "", tip);
			}
			
			@Override
			protected String doInBackground(Void... params) {
				return postRequest(url, paramsMap);
			}
			
			
			protected void onPostExecute(String result) {
				for (SimpleHttpCallback callBack : listCallBack) {
					if(callBack!=null)callBack.onResponseResult(command, result);
				}
				if (alertDialog != null && alertDialog.isShowing())
					alertDialog.dismiss();
			};
			
		}.execute();
	}
	
	
	public void addCallBack(SimpleHttpCallback callBack){
		listCallBack.add(callBack);
	}
	
	public void removeCallBack(SimpleHttpCallback callBack){
		listCallBack.remove(callBack);
	}
}
