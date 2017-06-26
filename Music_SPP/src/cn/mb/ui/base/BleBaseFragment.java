package cn.mb.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.sixpower.spp.ExpandDevice;
import cn.sixpower.spp.SppAppApplication;
import cn.sixpower.spp.SppAppConstant;
import cn.sixpower.spp.SppConnectHelper;
import cn.sixpower.spp.SppManager;
import cn.sixpower.spp.SppManagerCallBack;

public abstract class BleBaseFragment extends BaseFragment{
	protected SppManager bleManager;
	protected SppConnectHelper helper;
	protected View connectDiv,disConnectDiv;
	protected boolean clickEnter=false;
	public BleBaseFragment(boolean clickEnter){
		this.clickEnter=clickEnter;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		if(helper==null)helper = new SppConnectHelper("", callback);
		if(bleManager==null)bleManager = helper.getInstance();
		
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	private SppManagerCallBack callback = new SppManagerCallBack() {

		@Override
		public void occurrError(int paramInt, String paramString) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void discoverDevice(ExpandDevice paramExpandDevice) {
			
		}

		@Override
		public void scanTimeOut() {
			
		}

		@Override
		public void connectedAndReady() {
//			connectedAndReady();
//			initBleStatus();
		}

		@Override
		public void bindCallback(int paramInt) {
			
		}

		@Override
		public void connectLoss() {
//			connectLoss();
//			initBleStatus();
		}

		@Override
		public void connectTimeOut() {
			//failed
		}
		
		
	};
		
		public void connectedAndReady(){}
		public void connectLoss(){}
		
		@Override
		public void onResume() {
			super.onResume();
			initBleStatus();
		}
		@Override
		public void setAfter() {
			super.setAfter();
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(SppAppConstant.ACTION_BLE_CONNECT);
			intentFilter.addAction(SppAppConstant.ACTION_BLE_DISCONNECT);
			defaultActivity.registerReceiver(bleStatusReceiver, intentFilter);
		}
		protected void initBleStatus(){
//			if(connectDiv==null||disConnectDiv==null)return;
			
			if(SppAppApplication.mConnect){
//				connectDiv.setVisibility(View.VISIBLE);
//				disConnectDiv.setVisibility(View.GONE);
			}else{
//				disConnectDiv.setVisibility(View.VISIBLE);
//				connectDiv.setVisibility(View.GONE);
			}
		}
		
		public void setClickEnter(boolean clickEnter) {
			this.clickEnter = clickEnter;
		}
		
		@Override
		public void onDestroyView() {
			super.onDestroyView();
			if (bleStatusReceiver != null)
				defaultActivity.unregisterReceiver(bleStatusReceiver);
		}
		
//		if (updateDataReceive != null)
//			unregisterReceiver(updateDataReceive);
//		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction(AiduConstant.Action.REFRESH_BASEDATA);
//		intentFilter.addAction(AiduConstant.Action.BLE_STATUS_CHANGE_4_MAIN);
//		registerReceiver(updateDataReceive, intentFilter);
		
		
		private BroadcastReceiver bleStatusReceiver = new  BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				 if (action.equals(SppAppConstant.ACTION_BLE_DISCONNECT)) {
					 initBleStatus();
						return;
					} else if (action.equals(SppAppConstant.ACTION_BLE_CONNECT)) {
						initBleStatus();
						return;
					}
				
			}
		};
		
		
}
