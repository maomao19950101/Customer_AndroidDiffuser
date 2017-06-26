package cn.mb.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import cn.mb.app.AppConstant;
import cn.mb.http.PromptManager;

public class HttpUtil {
    public interface SimpleHttpCallback {
        void onResponseResult(int command, String resultStr);
    }

    // 创建HttpClient对象
    public static HttpClient httpClient = new DefaultHttpClient();

    // private static ExecutorService pool = Executors.newFixedThreadPool(5);
    public static synchronized String getRequest(String url) {
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

    public static synchronized String postRequest(String url, Map<String, String> params) {
        // 创建HttpPost对象。
        HttpPost post = new HttpPost(url);
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


    public static synchronized void postRequest2(final String url, final Map<String, String> paramsMap, final int command, final SimpleHttpCallback callBack, final String tip, final Context context) {
        new AsyncTask<Void, Void, String>() {
            private ProgressDialog alertDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
                alertDialog = PromptManager.getProgressDialog(context, "", tip);
            }

            @Override
            protected String doInBackground(Void... params) {
                return postRequest(url, paramsMap);
            }


            protected void onPostExecute(String result) {
                if (callBack != null) callBack.onResponseResult(command, result);
                if (alertDialog != null && alertDialog.isShowing())
                    alertDialog.dismiss();
            };

        }.execute();
    }

}
