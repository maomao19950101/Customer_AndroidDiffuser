package cn.mb.ui.common;

import android.os.Bundle;
import android.os.Handler;
import cn.mb.app.AppAplication;
import cn.mb.hk.music.R;
import cn.mb.ui.activity.WelcomeVideoActivity;
import cn.mb.ui.base.BaseActivity;
import cn.mb.util.CommUtils;

public class WelcomeActivity extends BaseActivity{

	
	private Handler handler = new Handler();
	private boolean flag =false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
//		
//		String endTime="2015-01-18 00:00:00";
//		Date  date = new Date();
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String now = df.format(date);
//		if(now.compareTo(endTime)>0){
//			setContentView(R.layout.vooda_loss);
//			return;
//		}
		
		setContentView(R.layout.welcome);
		if(AppAplication.tag)toLoginActivity();
		else
		loadingMain();
	}
	
	private Runnable  runnable=new Runnable() { // 延时进入主界面
		public void run() {
			toLoginActivity();
		}
	};
	private void toLoginActivity() {
//		Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
//		Intent intent = new Intent(WelcomeActivity.this, DiffUserActivity.class);
//		startActivity(intent);
		CommUtils.startActivity(WelcomeActivity.this, WelcomeVideoActivity.class);
		flag=true;
		AppAplication.tag=true;
		finish();
	}
	private void loadingMain() {
//		loadIndexData();
		if(!flag)handler.postDelayed(runnable,1000);
	}
	

	//TODO 加载数据 加載主頁的數據；然後存入application中，加載數據庫中的數據  加載了就存入數據庫
	/*List<ClassifyInfo>	classifyInfos;
	private void loadIndexData(){
		classifyInfos = new ArrayList<ClassifyInfo>();
		
		ClassifyInfo nfo = new ClassifyInfo();
		
		new HomeAsyncTask().execute();
	}*/
	
	
//	private class HomeAsyncTask extends AsyncTask<Void, Void, String> {
//		private ProgressDialog alertDialog;
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			alertDialog = PromptManager.getProgressDialog(WelcomeActivity.this, "", "正在加载");
//		}

//		@Override
//		protected String doInBackground(Void... params) {
//			return HttpUtil.getRequest(RequestUrls.NET_PVHOME_URL);
//			return "";
//		}

//		@Override
//		protected void onPostExecute(String result) {
//			RequestResult rs = JSON.parseObject(result, RequestResult.class);
//			if (rs.getStatus() == RequestResult.RequestResultStatus.REQUEST_SUCCESS) {
//			
//			
//			List<ClassifyInfo>	classifyInfos2 = SmallFunction.listKeyMaps(result, "Default", ClassifyInfo.class);
//			if(classifyInfos2!=null)classifyInfos.addAll(classifyInfos2);
//			
//			appContext.setMemCache("indexClassifyList", classifyInfos);
//				
//			} else {
//				VDNotic.alert(WelcomeActivity.this, rs.getMessage());
//			}
//			toLoginActivity();
//			if (alertDialog != null && alertDialog.isShowing())
//				alertDialog.dismiss();
//		}
//	}
}
