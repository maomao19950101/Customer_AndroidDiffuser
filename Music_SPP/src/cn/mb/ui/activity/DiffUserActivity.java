package cn.mb.ui.activity;

import java.util.ArrayList;
import java.util.Collections;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import cn.mb.app.AppAplication;
import cn.mb.hk.music.R;
import cn.mb.http.PromptManager;
import cn.mb.http.VDLog;
import cn.mb.http.VDNotic;
import cn.mb.ui.base.InBaseActivity;
import cn.mb.util.CommUtils;
import cn.sixpower.spp.ExpandDevice;
import cn.sixpower.spp.SppAppConstant;
import cn.sixpower.spp.SppConnectHelper;
import cn.sixpower.spp.SppError;
import cn.sixpower.spp.SppManager;
import cn.sixpower.spp.SppManagerCallBack;

public class DiffUserActivity extends InBaseActivity {

    private final String TAG = "SearchDevicesActivity";
    private ArrayList<ExpandDevice> devices = new ArrayList<ExpandDevice>();
    private MyAdapter adapter;
    private SppManager bleManager;
    private ListView list;

    private ProgressDialog alertDialog;
    private Button searchBtn;
    private boolean isConnecting = false;


    private SppConnectHelper helper;
    
    @Override
    protected void setAfter(Bundle savedInstanceState) {
        super.setAfter(savedInstanceState);
        list = (ListView) view.findViewById(R.id.aidu_scan_devices_list);
        adapter = new MyAdapter();
        
        list.setAdapter(adapter);
        
        
        
        if (helper == null) helper = new SppConnectHelper("", callback);
        searchBtn = (Button) view.findViewById(R.id.aidu_search_devices_btn);
       
        
       
        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isConnecting)
                    return;
                isConnecting = true;
                ExpandDevice d = devices.get(position);
                new ConnectAsyncTask().execute(d);
            }

        });

        searchBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickBtn();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (bleManager == null) bleManager = helper.getInstance();
        
        devices= bleManager.getPairList();adapter.notifyDataSetChanged();
    }
    private void onclickBtn() {
        if (AppAplication.mConnect) {
//            unbindDevice();
        } else {
            scan();
        }
    }
    private class ConnectAsyncTask extends AsyncTask<ExpandDevice, Void, String> {// 绑定设备
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = PromptManager.getProgressDialog(DiffUserActivity.this, "", R.string.aidu_device_connecting);
            alertDialog.setCancelable(true);
        }

        @Override
        protected String doInBackground(ExpandDevice... params) {
            ExpandDevice device = params[0];
            bleManager.stopScan();
            bleManager.disConnect();
            AppAplication.connectDeviceMac = device.getMacAddress();
            bleManager.connectDevice(device.getDevice());
            return null;
        }
    }
    
    public void scan() {
            devices.clear();
            devices = bleManager.getPairList();
            adapter.notifyDataSetChanged();
            bleManager.stopScan();
            bleManager.startScan(5, AppAplication.SCAN_DEVICE_NAME);
    }
    
    
	@Override
	protected int getLayoutResId() {
		return R.layout.pv_diffuser_devices;
	}
	private class MyAdapter extends BaseAdapter {
        private final class ViewHolder {
            TextView devicesName;
            TextView devicesStatus;
//            ImageView statusImg;
//            TextView devicesMac;
        }

        @Override
        public int getCount() {
            return devices.size();
        }

        @Override
        public Object getItem(int position) {
            return devices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            ExpandDevice d = devices.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(DiffUserActivity.this).inflate(R.layout.pv_diffuser_devices_item, null);
                holder = new ViewHolder();
                holder.devicesName = (TextView) convertView.findViewById(R.id.aidu_devices_list_name);
                holder.devicesStatus = (TextView) convertView.findViewById(R.id.aidu_devices_list_notic);
//                holder.statusImg = (ImageView) convertView.findViewById(R.id.aidu_devices_list_img);
//                holder.devicesMac = (TextView) convertView.findViewById(R.id.aidu_devices_list_mac);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.devicesName.setText(d.getDeviceName());
//            holder.devicesMac.setText(d.getMacAddress());
//            holder.statusImg.setTag(d);
//            if (d != null && AppAplication.connectDeviceMac != null && d.getMacAddress().equals(AppAplication.connectDeviceMac)) {
//                holder.devicesStatus.setText("已经连接");
//            } else {
//                holder.devicesStatus.setText("未连接");
//            }
            
           int  connectState = d.getDevice().getBondState();
            switch (connectState) {

		        case BluetoothDevice.BOND_NONE:
		            // Preparing
//		            try {
//		                Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
//		                createBondMethod.invoke(device);
//		            } catch (Exception e) { 
//		                e.printStackTrace();
//		            }
		        	holder.devicesStatus.setText("未配对");
		            break;
		        // Prepared
		        case BluetoothDevice.BOND_BONDED:
//		            try {
//		                connect(device);
//		            } catch (IOException e) {
//		                e.printStackTrace();
//		            }
		        	holder.devicesStatus.setText("已配对");
		            break;
		        case BluetoothDevice.BOND_BONDING:
		        	 holder.devicesStatus.setText("BOND_BONDING");
		        	break;
            }

            return convertView;
        }

    }
	
	private SppManagerCallBack callback = new SppManagerCallBack() {

        @Override
        public void occurrError(int paramInt, String paramString) {
            VDNotic.alert(DiffUserActivity.this, SppError.getErrorMsgByCode(paramInt));
        }

        @Override
        public void discoverDevice(ExpandDevice paramExpandDevice) {
            addOrUpdateDevice(paramExpandDevice);
            sortDeviceByRssi();
            adapter.notifyDataSetChanged();
        }

        @Override
        public void scanTimeOut() {
//            VDLog.msg(DiffUserActivity2.this, "扫描完成");
        }

        @Override
        public void connectedAndReady() {// 连接成功 此页面
            VDLog.msg(DiffUserActivity.this, "Connection Succeeded");
            VDLog.i(TAG, "蓝牙连接成功");
            initStatus();
            sendBroadcast(new Intent(SppAppConstant.ACTION_BLE_CONNECT));
            startActivity();
        }

        @Override
        public void bindCallback(int paramInt) {
            sendBroadcast(new Intent(SppAppConstant.ACTION_BLE_CONNECT));
            finish();
        }

        @Override
        public void connectLoss() {
            VDLog.msg(DiffUserActivity.this, "Disconnected");
            AppAplication.connectDeviceMac = null;
            sendBroadcast(new Intent(SppAppConstant.ACTION_BLE_DISCONNECT));
        }

        @Override
        public void connectTimeOut() {
//failed
        	   VDNotic.alert(DiffUserActivity.this, "failed");
        }
    };
    // 清除加载，清除动画，清除标记
    private void initStatus() {
        if (alertDialog != null && alertDialog.isShowing())
            alertDialog.dismiss();
        isConnecting = false;


    }
    private void startActivity() {
    /*	 for (Activity item : AppAplication.listActivity) {
             if(item instanceof VideoWelcomeActivity) item.finish();
          }*/
        Intent intent = new Intent(DiffUserActivity.this, MainActivity.class);
        CommUtils.startActivity(DiffUserActivity.this, intent);
        finish();
    }
    // 排序
    private void sortDeviceByRssi() {
        for (int i = devices.size(); i > 1; i--) {
            for (int j = 1; j < i; j++) {
                if (devices.get(j).getRssi() > devices.get(j - 1).getRssi()) {// 交换
                    Collections.swap(devices, j - 1, j);
                }
            }
        }
    }
    private void addOrUpdateDevice(ExpandDevice expandDevice) {
        int index = devices.indexOf(expandDevice);
        if (index >= 0)
            devices.get(index).setRssi(expandDevice.getRssi());
//        else if (expandDevice.getDeviceName().contains("PZ"))
            devices.add(expandDevice);
    }
    
    
    public void startM(View v){
    	startActivity();
    }
}
