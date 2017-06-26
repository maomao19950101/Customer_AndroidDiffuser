package cn.sixpower.spp;

import java.util.ArrayList;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import cn.mb.app.AppAplication;
import cn.sixpower.spp.SppService.ResponseOrNotifyListener;

public class SppManager {
    private SppManager sppManager;
    private SppService mService;
    private SppManagerCallBack callBack;
    private String TAG = "SppManager";
    private Context context;
    private BluetoothAdapter mBluetoothAdapter;
    private ServiceConnection conn = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            Log.e(SppManager.this.TAG, "BleService Disconnected");
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            SppManager.this.mService = ((SppService.LocalBinder) service).getService();
            if (SppManager.this.callBack != null)
                SppManager.this.mService.setCallBack(SppManager.this.callBack);
        }
    };

    public SppManager getSppManager(SppManagerCallBack callBack) {
        if (sppManager != null) {
            if (callBack != null) mService.setCallBack(callBack);

        } else {

            sppManager = new SppManager(SppAppApplication.getBleApplication(), callBack);
        }

        return sppManager;
    }

    public SppManager(Context context, SppManagerCallBack callBack) {
        System.out.println("-------------" + callBack.toString());


        if (context != null) {
            context.bindService(new Intent(context, SppService.class), this.conn, this.context.BIND_AUTO_CREATE);
            this.callBack = callBack;
            this.context = context;
        }
    }

    public boolean isBlueToothAvailable() {
//		BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();// 获取手机默认上的蓝牙适配器
        if (mBluetoothAdapter == null) {// 蓝牙不可用
            return false;
        }

        return true;
    }

    /**
     * 打开蓝牙
     */
    public void openBluetooth() {
        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
            }
        }
    }


    public void startScan(int time, String filter_condition) {
//		this.mService.SCAN_TIME = (time > 0 ? time * 1000 : this.mService.SCAN_TIME);
		this.mService.SCAN_DEVICE_NAME = filter_condition;
        this.mService.scan(true);
    }

    public void stopScan() {
        this.mService.scan(false);
    }

    public void connectDevice(BluetoothDevice device) {
        if (device == null)
            return;
        if (this.mService == null)
            return;
//        this.mService.connect(device);
        this.mService.connect(mBluetoothAdapter.getRemoteDevice(device.getAddress()));
    }

    public void disConnect() {
        this.mService.stop();
    }

    public void writeData(byte[] data, boolean needRetry) {
        if (this.mService == null) return;
        this.mService.sendHex(data);
    }

    public int getToothState() {
        return this.mService.getState();
    }

    public void setResponseOrNotifyListener(ResponseOrNotifyListener responseOrNotifyListener) {
        this.mService.setResponseOrNotifyListener(responseOrNotifyListener);
    }


    
    public ArrayList<ExpandDevice> getPairList(){//pairedDevices
    	ArrayList<ExpandDevice> pairExpandDeviceList = new 	ArrayList<ExpandDevice>();
    	if(isBlueToothAvailable()){
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		
		for (BluetoothDevice bluetoothDevice : pairedDevices) {
			if(bluetoothDevice.getName().contains(AppAplication.SCAN_DEVICE_NAME))
				pairExpandDeviceList.add(new ExpandDevice(bluetoothDevice.getAddress(), bluetoothDevice.getName(), 55, bluetoothDevice));
		}
    	}
    	
    	return pairExpandDeviceList;
    }
}
