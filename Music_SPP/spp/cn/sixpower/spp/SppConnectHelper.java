package cn.sixpower.spp;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import cn.mb.http.VDLog;
import cn.mb.http.VDNotic;

public class SppConnectHelper {

	private static SppManager sppManager;
	private Context context;
	private static String TAG = "BLEConnectHelper";
	private SppManagerCallBack sppCallBack;// 页面回调用
	private boolean hasMac=false;
	private SppManagerCallBack callBack = new SppManagerCallBack() {

		public void occurrError(int paramInt, String paramString) {
			VDLog.i(TAG, "occurrError...paramInt：" + paramInt + "   paramString:" + paramString);
			if (sppCallBack != null)
				sppCallBack.occurrError(paramInt, paramString);
		}

		public void discoverDevice(ExpandDevice paramExpandDevice) {
			if (sppCallBack != null)
				sppCallBack.discoverDevice(paramExpandDevice);
			if (paramExpandDevice != null) {
				String mac = paramExpandDevice.getMacAddress();
				if(mac!=null&&!mac.equals("")&&mac.equals(SppAppApplication.connectDeviceMac)){
					
					hasMac = true;
					sppManager.stopScan();

					VDLog.i(TAG, "找到相同的蓝牙..."+mac+"     信号 " + paramExpandDevice.getRssi());
					
					handler.obtainMessage(1, paramExpandDevice.getDevice()).sendToTarget();
				}
			}

		}

		public void scanTimeOut() {

			if(!hasMac){
				VDLog.i(TAG, "没有相同的蓝牙...");
			}else{
//				handler.sendEmptyMessage(1);
			}
			if (sppCallBack != null)
				sppCallBack.scanTimeOut();
		}

		public void connectedAndReady() {
			if (sppCallBack != null)
				sppCallBack.connectedAndReady();

		}

		public void bindCallback(int paramInt) {

			if (sppCallBack != null)
				sppCallBack.bindCallback(paramInt);
		}

		public void connectLoss() {
			if (sppCallBack != null)
				sppCallBack.connectLoss();
		}

		public void connectTimeOut() {
			if (sppCallBack != null)
				sppCallBack.connectTimeOut();
		}

	};

	public SppManager getInstance() {
		sppManager = new SppManager(context, callBack);
		return sppManager;
	}

	public SppConnectHelper(String bindBleMac, SppManagerCallBack sppCallBack) {
		this.sppCallBack = sppCallBack;
		this.context=SppAppApplication.getBleApplication();
	}

	public void reConnect() {
//		isReconnect = true;
		handler.sendEmptyMessage(0);
	}

	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
		switch (msg.what) {// 1 连接 2 解绑 3 断开连接 4 完成
		case 0:
			VDLog.i(TAG, "搜索...");
			sppManager.startScan(10, "");
			break;
		case 1:
//			VDNotic.alert(context, R.string.aidu_device_connecting);
			VDNotic.alert(context,  "连接...");
			VDLog.i(TAG, "连接...");
			sppManager.connectDevice((BluetoothDevice)msg.obj);
	break;
		case 2:
			VDLog.i(TAG, "正在解绑...");
//			bleManager.unBindDevice(true);
			break;
		case 3:
			VDLog.i(TAG, "断开连接...");
			sppManager.disConnect();
			break;
		case 4:
//			AiduApplication.isUnbinding=false;
			VDLog.i(TAG, "unbind完成...");
//			if(blecallback!=null)blecallback.unbindBack();
			break;
		default:
			break;
		}
	};
	};
}
