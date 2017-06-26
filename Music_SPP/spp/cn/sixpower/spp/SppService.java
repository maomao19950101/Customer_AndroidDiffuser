package cn.sixpower.spp;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import android.app.Service;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;
import cn.mb.app.AppAplication;
import cn.mb.http.VDLog;
import cn.mb.util.ArrayUtils;

public class SppService extends Service {
    private static final String TAG = "BluetoothChatService";

    private static final String NAME = "BluetoothSpp";

    private UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    protected String SCAN_DEVICE_NAME = "";
    private BluetoothAdapter mAdapter;
    //	private final Handler mHandler;
    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private int mState;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0; // we're doing nothing
    public static final int STATE_LISTEN = 1; // now listening for incoming  connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing  connection
    public static final int STATE_CONNECTED = 3; // now connected to a remote  device
    private static final int MESSAGE_STATE_CHANGE = 4;
//    private static final int MESSAGE_TOAST = 5;
//    public static final String TOAST = "toast";

    private static final int SPP_STATE_STOP_SCAN = 11;
//    private static final int SPP_STATE_CONNECT_TIME_OUT = 12;
//    private static final int SPP_STATE_RE_WRITE = 13;
    private static final int SPP_STATE_FOUND_DEVICE = 14;
    private static final int SPP_STATE_CONNECT_LOST = 15;
    private static final int SPP_STATE_FAILED = 16;
//    private static final int SPP_STATE_CONNECT_SUCCESS = 17;
//    private static final int SPP_STATE_BIND = 18;
    private SppManagerCallBack callBack;
    private String mMacAddress;
    private String mDeviceName;


    private Object lock = new Object();
    private OutputStream outhex;//发送十六进制
    private BufferedWriter out;//发送String

//    private BluetoothDevice remotedevice = null;

    public SppService() {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;

        mAdapter.getProfileProxy(AppAplication.getApplication(), mProfileListener, BluetoothProfile.A2DP);
        mAdapter.getProfileProxy(AppAplication.getApplication(), mProfileListener, BluetoothProfile.HEADSET);
        mAdapter.getProfileConnectionState(BluetoothProfile.A2DP);
        mAdapter.getProfileConnectionState(BluetoothProfile.HEADSET);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    public class LocalBinder extends Binder {
        protected LocalBinder() {
        }

        public SppService getService() {
            return SppService.this;
        }
    }

    public SppService(Context context) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
    }

    /**
     * Set the current state of the chat connection
     *
     * @param state An integer defining the current connection state
     */

    private synchronized void setState(int state) {
        VDLog.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;

//        handler.sendEmptyMessage(MESSAGE_STATE_CHANGE);
        handler.obtainMessage(MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    /**
     * Return the current connection state.
     */
    public synchronized int getState() {
        return mState;
    }

    /**
     * Start the  service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    public synchronized void start() {
        VDLog.d(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to listen on a BluetoothServerSocket
        if (mAcceptThread == null) {
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }
        setState(STATE_LISTEN);
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     *
     * @param device The BluetoothDevice to connect
     */
    public synchronized void connect(BluetoothDevice device) {
        VDLog.d(TAG, "connect to: " + device);

        if (checkBlueToothEnable(true) != 0) {
            return;
        }
//        if (device == null) {
//            return;
//        }
        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        
        this.mMacAddress = device.getAddress();
        this.mDeviceName = device.getName();
//        remotedevice = socket.getRemoteDevice();
        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made
     * @param device The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        VDLog.d(TAG, "connected");

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Cancel the accept thread because we only want to connect to one
        // device
        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }

        // Start the thread to manage the connection and perform
        // transmissions
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.setPriority(Thread.MAX_PRIORITY);
        mConnectedThread.start();

        // Send the name of the connected device back to the UI Activity

        this.mMacAddress = device.getAddress();
        this.mDeviceName = device.getName();
        remotedevice = socket.getRemoteDevice();


        handler.sendEmptyMessage(STATE_CONNECTED);
        setState(STATE_CONNECTED);
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        VDLog.d(TAG, "stop");
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }
        setState(STATE_NONE);
    }


    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() {
        setState(STATE_LISTEN);
        handler.sendEmptyMessage(SPP_STATE_FAILED);
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        setState(STATE_LISTEN);

        handler.sendEmptyMessage(SPP_STATE_CONNECT_LOST);
    }

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted (or
     * until cancelled).
     */
    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmpServerSocket = null;

            // Create a new listening server socket

            try {
                if (Build.VERSION.SDK_INT >= 10) {
                    tmpServerSocket = mAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME, MY_UUID);
                } else {
                    tmpServerSocket = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
                }
            } catch (IOException e) {
                VDLog.e(TAG, "listen() failed", e);
            }
            mmServerSocket = tmpServerSocket;
        }

        public void run() {
            VDLog.d(TAG, "BEGIN mAcceptThread" + this);
            setName("AcceptThread");
            BluetoothSocket socket = null;

            // Listen to the server socket if we're not connected
            while (mState != STATE_CONNECTED) {
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    VDLog.e(TAG, "accept() failed", e);
                    break;
                }

                // If a connection was accepted
                if (socket != null) {
                    synchronized (SppService.this) {
                        switch (mState) {
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                // Situation normal. Start the connected thread.
                                remotedevice = socket.getRemoteDevice();
                                connected(socket, socket.getRemoteDevice());
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                // Either not ready or already connected.
                                // Terminate new socket.
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    VDLog.e(TAG, "Could not close unwanted socket", e);
                                }
                                break;
                        }
                    }
                }
            }
            VDLog.i(TAG, "END mAcceptThread");
        }

        public void cancel() {
            VDLog.d(TAG, "cancel " + this);
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                VDLog.e(TAG, "close() of server failed", e);
            }
        }
    }

    /**
     * This thread runs while attempting to make an outgoing connection with a
     * device. It runs straight through; the connection either succeeds or
     * fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmpBtSocket = null;
            VDLog.i("MMMMMMMMMMMMMMMMMMMM", "Q TO GC B P JFD =" + tmpBtSocket);
            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            // device = mAdapter.getRemoteDevice("00:15:FF:F3:09:97");

            if (Build.VERSION.SDK_INT >= 10) {
                try {
                    tmpBtSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                    if (tmpBtSocket == null) {
                        tmpBtSocket = fanxieNewInsecureSocket(device);
                        VDLog.i("fanxie", "fanxieNewInsecureSocket");
                        if (tmpBtSocket == null) {
                            tmpBtSocket = fanxieOldInsecureSocket(device);
                            VDLog.i("fanxie", "fanxieOldInsecureSocket");
                            if (tmpBtSocket == null) {
                                tmpBtSocket = fanxieNewSecureSocket(device);
                                VDLog.i("fanxie", "fanxieNewSecureSocket");
                                if (tmpBtSocket == null) {
                                    tmpBtSocket = fanxieOldSecureSocket(device);
                                    VDLog.i("fanxie", "fanxieOldSecureSocket");
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (Build.VERSION.SDK_INT < 10) {
                try {
                    tmpBtSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                    if (tmpBtSocket == null) {
                        tmpBtSocket = fanxieNewInsecureSocket(device);
                        VDLog.i("fanxie", "fanxieNewInsecureSocket");
                        if (tmpBtSocket == null) {
                            tmpBtSocket = fanxieOldInsecureSocket(device);
                            VDLog.i("fanxie", "fanxieOldInsecureSocket");
                            if (tmpBtSocket == null) {
                                tmpBtSocket = fanxieNewSecureSocket(device);
                                VDLog.i("fanxie", "fanxieNewSecureSocket");
                                if (tmpBtSocket == null) {
                                    tmpBtSocket = fanxieOldSecureSocket(device);
                                    VDLog.i("fanxie", "fanxieOldSecureSocket");
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

                tmpBtSocket = fanxieNewInsecureSocket(device);
                VDLog.i("fanxie", "fanxieNewInsecureSocket");
                if (tmpBtSocket == null) {
                    tmpBtSocket = fanxieOldInsecureSocket(device);
                    VDLog.i("fanxie", "fanxieOldInsecureSocket");
                    if (tmpBtSocket == null) {
                        tmpBtSocket = fanxieNewSecureSocket(device);
                        VDLog.i("fanxie", "fanxieNewSecureSocket");
                        if (tmpBtSocket == null) {
                            tmpBtSocket = fanxieOldSecureSocket(device);
                            VDLog.i("fanxie", "fanxieOldSecureSocket");
                        }
                    }
                }

            }
            mmSocket = tmpBtSocket;
        }

        public void run() {
            VDLog.i(TAG, "BEGIN mConnectThread");
            setName("ConnectThread");
            // Always cancel discovery because it will slow down a
            // connection
            mAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                VDLog.i("kent", "我!=" + mmSocket);
                // SystemClock.sleep(1000);
                VDLog.i("kent", "我!1000=" + mmSocket);
                mmSocket.connect();
                VDLog.i("kent", "Connected!");
            } catch (IOException e) {
                connectionFailed();
                VDLog.i("connectionLost", "Unable to connect device开");
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    VDLog.e(TAG, "unable to close() socket during connection failure", e2);
                }
                // Start the service over to restart listening mode
                SppService.this.start();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (SppService.this) {
                mConnectThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                VDLog.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device. It handles all
     * incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
//		private int availableBytes = 0;

        // private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            VDLog.d(TAG, "create ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            // OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                outhex = socket.getOutputStream();
            } catch (IOException e) {
                VDLog.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            // mmOutStream = tmpOut;
        }


        /**
         * Convert byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
         *
         * @param src byte[] data
         * @return hex string
         */
        private String bytesToHexString(byte[] src) {
            StringBuilder stringBuilder = new StringBuilder("");
            if (src == null || src.length <= 0) {
                return null;
            }
            for (int i = 0; i < src.length; i++) {
                int v = src[i] & 0xFF;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    stringBuilder.append(0);
                }
                stringBuilder.append(hv);
            }
            return stringBuilder.toString();
        }

        public void run() {
            VDLog.i(TAG, "BEGIN mConnectedThread");
            int bytes = 0;
            while (true) {
                synchronized (lock) {
                    try {
                        byte[] buffer = new byte[1024];
                        bytes = mmInStream.read(buffer);//注意判断 mmInStream 为空的时候
                        VDLog.i(TAG, "收到的数据:" + bytesToHexString(buffer));
                        VDLog.i("wqmmd", String.valueOf(bytes));
                        if (bytes > 0) {
//							SppService.this.paraseDataBefore(buffer.clone(),bytes );
                            SppService.this.paraseDataBefore(buffer.clone());
                            buffer = null;
                        }

                    } catch (IOException e) {
                        VDLog.e(TAG, "disconnected", e);
                        connectionLost();
                        break;
                    }
                }
            }
        }


        public void cancel() {
            try {
                // fileOutputStream.close();
                mmSocket.close();

            } catch (IOException e) {
                VDLog.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    public void sendString(String content) {

        if (mConnectedThread != null) {
            try {
//				mConnectedThread.write(buffer);
                out.write(content);
                out.flush();
                VDLog.i(TAG, "---sendString--: " + content);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void sendHex(byte[] hexStringToBytes) {

        if (mConnectedThread != null) {
            try {
                outhex.write(hexStringToBytes);
                outhex.flush();
                VDLog.i(TAG, "---sendHex--: " + hexStringToBytes.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private BluetoothSocket fanxieNewInsecureSocket(BluetoothDevice device) {
        Method method;

        try {
            method = BluetoothDevice.class.getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{int.class});
        } catch (SecurityException e1) {
            e1.printStackTrace();
            return null;
        } catch (NoSuchMethodException e) {

            e.printStackTrace();
            return null;
        }
        try {
            return (BluetoothSocket) method.invoke(device, Integer.valueOf(1));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }

    }

    private BluetoothSocket fanxieOldInsecureSocket(BluetoothDevice device) {
        Method method;

        try {
            method = BluetoothDevice.class.getMethod("createInsecureRfcommSocket", new Class[]{int.class});
        } catch (SecurityException e1) {
            e1.printStackTrace();
            return null;
        } catch (NoSuchMethodException e) {

            e.printStackTrace();
            return null;
        }
        try {
            return (BluetoothSocket) method.invoke(device, Integer.valueOf(1));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }

    }

    private BluetoothSocket fanxieNewSecureSocket(BluetoothDevice device) {

        Method method;

        try {
            method = BluetoothDevice.class.getMethod("createRfcommSocketToServiceRecord", new Class[]{int.class});
        } catch (SecurityException e1) {
            e1.printStackTrace();
            return null;
        } catch (NoSuchMethodException e) {

            e.printStackTrace();
            return null;
        }
        try {
            return (BluetoothSocket) method.invoke(device, Integer.valueOf(1));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }

    }

    private BluetoothSocket fanxieOldSecureSocket(BluetoothDevice device) {

        Method method;

        try {
            method = BluetoothDevice.class.getMethod("createRfcommSocket", new Class[]{int.class});
        } catch (SecurityException e1) {
            e1.printStackTrace();
            return null;
        } catch (NoSuchMethodException e) {

            e.printStackTrace();
            return null;
        }
        try {
            return (BluetoothSocket) method.invoke(device, Integer.valueOf(1));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }

    }


    private boolean mIsScaning;
    protected long SCAN_TIME = 10000L;

    public void scan(boolean flag) {

        if (checkBlueToothEnable(true) != 0) {
            return;
        }

        if (flag) {
            if (this.mIsScaning) {
                return;
            }
            this.mAdapter.startDiscovery();
            this.mIsScaning = true;
            this.handler.sendEmptyMessageDelayed(1, this.SCAN_TIME);
        } else {
            this.handler.removeMessages(1);
            stopScan();
        }
    }

    private void stopScan() {
        if (this.mAdapter.isDiscovering()) {
            this.mAdapter.cancelDiscovery();
        }
        this.mIsScaning = false;
    }

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SPP_STATE_STOP_SCAN:// 停止扫描
                    SppService.this.stopScan();
                    if (SppService.this.callBack != null) SppService.this.callBack.scanTimeOut();
                    break;
//                case SPP_STATE_CONNECT_TIME_OUT:// 连接超时
                case SPP_STATE_FAILED:
                case STATE_NONE:
				/*SppService.this.mErrorState = SppError.CONNECT_TIME_OUT.getErrorCode();
				synchronized (SppService.this.mLock) {
					SppService.this.mLock.notifyAll();
				}*/
                    if (SppService.this.callBack != null) SppService.this.callBack.connectTimeOut();
                    break;
                    /*     case SPP_STATE_RE_WRITE://重发指令
				BleService.this.mBusy = false;
				if (BleService.this.reWriteCount < 5) {
					BleService.this.reWriteCount += 1;
					VDLog.v(BleService.this.TAG, "重发" + BleService.this.reWriteCount);
					BleService.this.writeData(BleService.this.mData, true);
				} else {
					BleService.this.reWriteCount = 0;
					if ((BleService.this.mData[2] == 7) && (BleService.this.mData[3] == 1)) {
						BleService.this.disConnected();
						BleService.this.callBack.bindCallback(3);
						return;
					}

					if ((BleService.this.cachePool.size() > 0)
							&& (((byte[]) BleService.this.cachePool.getFirst()).equals(BleService.this.mData))) {
						VDLog.w(BleService.this.TAG, "未收到回复，移除缓存" + Arrays.toString(BleService.this.mData));
						BleService.this.cachePool.removeFirst();
					}
				}
                    break;*/
                case SPP_STATE_FOUND_DEVICE:// 发现设备
                    if (SppService.this.callBack != null)
                        SppService.this.callBack.discoverDevice((ExpandDevice) msg.obj);
                    break;
                case SPP_STATE_CONNECT_LOST:// 连接丢失
                    SppAppApplication.connectDeviceMac = null;
                    SppAppApplication.isReconnect = false;
                    SppAppApplication.mConnect = false;
                    sendBroadcast(new Intent(SppAppConstant.ACTION_BLE_DISCONNECT));
                    if (SppService.this.callBack != null) SppService.this.callBack.connectLoss();
                    stop();
                    break;
                case STATE_CONNECTED:
                    SppAppApplication.connectDeviceMac = mMacAddress;
                    SppAppApplication.connectDeviceName = mDeviceName;
                    SppAppApplication.isReconnect = true;
                    SppAppApplication.mConnect = true;
                    sendBroadcast(new Intent(SppAppConstant.ACTION_BLE_CONNECT));
                    if (SppService.this.callBack != null)
                        SppService.this.callBack.connectedAndReady();
                    
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            s("");
                        }
                    }, 5000);
                    break;
                    
                case MESSAGE_STATE_CHANGE:
                	
                	switch (msg.arg1) {
                    case STATE_CONNECTED:
                    	   SppAppApplication.connectDeviceMac = mMacAddress;
                           SppAppApplication.connectDeviceName = mDeviceName;
                           SppAppApplication.isReconnect = true;
                           SppAppApplication.mConnect = true;
                           sendBroadcast(new Intent(SppAppConstant.ACTION_BLE_CONNECT));
                           if (SppService.this.callBack != null)
                               SppService.this.callBack.connectedAndReady();
//                        conntxt.setText(R.string.title_connected_to);
//                        if(mConnectedDeviceName != null) conntxt.append(mConnectedDeviceName);
//                        connb.setText(R.string.disconnectButton);
//                        
//                        tabs.getTabWidget().getChildAt(VIEW_SETT).setVisibility(View.VISIBLE);
//                          <string name="title_connected_to">蓝牙设备已连接到</string>
//                        <string name="disconnectButton">断开连接</string>
                        break;
                    case STATE_CONNECTING:
//                    	conntxt.setText(R.string.title_connecting);
//                    	 <string name="title_connecting">蓝牙设备连接中...</string>
                        break;
                    case STATE_LISTEN:
                    case STATE_NONE:
//                    	conntxt.setText(R.string.title_not_connected);
//                    	connb.setText(R.string.connButton);
//                    	
//                    	tabs.setCurrentTab(VIEW_BT);
//                    	tabs.getTabWidget().getChildAt(VIEW_SETT).setVisibility(View.GONE);
//                    	tabs.getTabWidget().getChildAt(VIEW_PLAYER).setVisibility(View.GONE);
//                    	tabs.getTabWidget().getChildAt(VIEW_FM).setVisibility(View.GONE);
//                    	 <string name="title_not_connected">蓝牙设备未连接</string>
//                    	 <string name="connButton">连接设备</string>
                    	break;
                    }
                	break;
            }
        }
    };

    private void paraseDataBefore(final byte[] value)// 修改
    {
        this.handler.post(new Runnable() {
            public void run() {
                SppService.this.paraseData(value);
            }
        });
    }

    protected void paraseData(byte[] value) {
//		int[] data = new int[value.length];
        int[] data = new int[15];
        for (int i = 0; i < data.length; i++) {
            data[i] = Integer.parseInt(String.format("%02X", new Object[]{Byte.valueOf(value[i])}), 16);
        }
        VDLog.i("SppService", "收到数据》》"+Arrays.toString(value));
        VDLog.i("SppService", "收到数据int》》"+Arrays.toString(data));
//        if (responseOrNotifyListener != null) responseOrNotifyListener.responseOrNotify(data);

        for (ResponseOrNotifyListener l : responseOrNotifyListenerList) {
        	VDLog.i("SppService", "执行回调");
			l.responseOrNotify(data);
		}
    }

    public void setCallBack(SppManagerCallBack callBack) {
        this.callBack = callBack;
        checkBlueToothEnable(true);

    }

    private int checkBlueToothEnable(boolean sendError) {
        int state = 0;
        if (this.mAdapter == null) {
			/*BluetoothManager bluetoothManager = (BluetoothManager) getSystemService("bluetooth");
			if (bluetoothManager == null){
				state = 101;
			
			}*/
            this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        }

        if (!this.mAdapter.isEnabled())
            state = 103;
        if ((sendError) && (state != 0)) {
            this.handler.sendMessage(this.handler.obtainMessage(6, state, 0, SppError.getErrorMsgByCode(state)));
        }
        return state;
    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            System.out.println("action " + action);

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, (short) 0);
                String deviceName = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
//				if(device==null) deviceName =device.getName();
                if (deviceName.contains(SppService.this.SCAN_DEVICE_NAME)) {
                    ExpandDevice device2 = new ExpandDevice(device.getAddress(), deviceName, rssi, device);
                    SppService.this.handler.sendMessage(SppService.this.handler.obtainMessage(SPP_STATE_FOUND_DEVICE, device2));
                }

                return;
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                SppService.this.handler.sendEmptyMessage(SPP_STATE_STOP_SCAN);
            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                SppService.this.handler.sendEmptyMessage(STATE_CONNECTED);
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            	 SppAppApplication.connectDeviceMac = null;
                 SppAppApplication.isReconnect = false;
                 SppAppApplication.mConnect = false;
                 sendBroadcast(new Intent(SppAppConstant.ACTION_BLE_DISCONNECT));
                 if (SppService.this.callBack != null) SppService.this.callBack.connectLoss();
                 stop();
            	
//                SppService.this.handler.sendEmptyMessage(SPP_STATE_CONNECT_LOST);
            } else if (BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED.equalsIgnoreCase(action)) {
                final BluetoothDevice mdevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int istate = intent.getIntExtra(BluetoothA2dp.EXTRA_STATE, -1);
                int newState = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, 0);
                VDLog.i(TAG, "BluetoothA2dp ACTION_CONNECTION_STATE_CHANGED ：" + istate + "|" + newState + "," + mdevice);
                if (mdevice != null&&newState!=0) {// &&istate==2&&newState==2
//            	 checkBlueToothA2dpState(mdevice,istate);
//                	connect(mAdapter.getRemoteDevice(mdevice.getAddress()));
                	 if (!AppAplication.mConnect) {
                		 
                		 handler.postDelayed(new Runnable() {
                             public void run() {
                            	 connect(mdevice);
                             }
                         }, 1000);
                		 
                	 }
                }
            } else if (BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED.equalsIgnoreCase(action)) {
                BluetoothDevice mdevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int istate = intent.getIntExtra(BluetoothHeadset.EXTRA_STATE, -1);
                int newState = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, 0);
                VDLog.i(TAG, "BluetoothHeadset ACTION_CONNECTION_STATE_CHANGED ：" + istate + "|" + newState + "," + mdevice);
                if (mdevice != null) {
//            	 checkBlueToothA2dpState(mdevice,istate);
                    if (!AppAplication.mConnect) connect(mdevice);
                }
            }

        }
    };

    @Override
    public void onCreate() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_CLASS_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED);
        filter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);

        this.registerReceiver(mReceiver, filter);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if (mReceiver != null) unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public static abstract interface ResponseOrNotifyListener {
        public abstract void responseOrNotify(int[] data);
    }

    private List<ResponseOrNotifyListener> responseOrNotifyListenerList=new ArrayList<SppService.ResponseOrNotifyListener>();

    public void setResponseOrNotifyListener(ResponseOrNotifyListener responseOrNotifyListener) {
    	 this.responseOrNotifyListenerList.clear();
        this.responseOrNotifyListenerList.add(responseOrNotifyListener);
    }

    public boolean onUnbind(Intent intent) {
        this.stop();
        this.callBack = null;
//        this.responseOrNotifyListener = null;
        this.responseOrNotifyListenerList.clear();
        return super.onUnbind(intent);
    }

    private BluetoothA2dp mBluetoothA2dp = null;
    private BluetoothHeadset mBluetoothHeadset = null;
    private BluetoothProfile.ServiceListener
            mProfileListener = new BluetoothProfile.ServiceListener() {
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            if (profile == BluetoothProfile.HEADSET) {
                mBluetoothHeadset = (BluetoothHeadset) proxy;

//	            if(null != mBluetoothHeadset.getConnectedDevices()){
//	            	conn_status_hfp = BluetoothProfile.STATE_CONNECTED;
//	            }

                List<BluetoothDevice> devices = mBluetoothHeadset.getConnectedDevices();
                if (null != devices && devices.size() > 0) {
//	        		conn_status_a2dp = BluetoothProfile.STATE_CONNECTED;	
//	        		try {	
                    if (!AppAplication.mConnect) connect(devices.get(0));
                    //BluetoothSocket
//						devices.get(0).createRfcommSocketToServiceRecord(MY_UUID);
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
                }
            } else if (profile == BluetoothProfile.A2DP) {
                mBluetoothA2dp = (BluetoothA2dp) proxy;
                List<BluetoothDevice> devices = mBluetoothA2dp.getConnectedDevices();
                if (null != devices && devices.size() > 0) {
                    if (!AppAplication.mConnect)
                        connect(devices.get(0));    //mBluetoothA2dp  板子问题音频问题
//	        		conn_status_a2dp = BluetoothProfile.STATE_CONNECTED;	
//	        		try {	
//	        			connect(devices.get(0));	;//BluetoothSocket
//						devices.get(0).createRfcommSocketToServiceRecord(MY_UUID);
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
                }
            }
        }

        public void onServiceDisconnected(int profile) {
            if (profile == BluetoothProfile.HEADSET) {
                mBluetoothHeadset = null;
            } else if (profile == BluetoothProfile.A2DP) {
                mBluetoothA2dp = null;
            }
        }
    };

    private BluetoothDevice remotedevice = null;

    public void s(String mac) {//a2dpbut
//		mAdapter.getProfileProxy(getApplicationContext(), mProfileListener, BluetoothProfile.A2DP);
        //mBluetoothA2dp.connect(remotedevice);
//		BluetoothDevice	remotedevice2 = mAdapter.getRemoteDevice(mac);
        if (remotedevice == null) {
            Toast.makeText(getApplicationContext(), "请先选择音频设备", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mBluetoothA2dp != null) {
            Class<? extends BluetoothA2dp> clazz = mBluetoothA2dp.getClass();
            Method m2;
            if (BluetoothProfile.STATE_CONNECTED == mBluetoothA2dp.getConnectionState(remotedevice)) {
			/*try{
				m2 = clazz.getMethod("disconnect", BluetoothDevice.class);
				m2.invoke(mBluetoothA2dp, remotedevice);						
			}catch(Exception e){
				e.printStackTrace();
			}*/
            } else {
                try {
                    m2 = clazz.getMethod("connect", BluetoothDevice.class);
                    m2.invoke(mBluetoothA2dp, remotedevice);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
		
/*板子问题音频问题 注释
 * 		if(mBluetoothHeadset!=null){
		Class<? extends BluetoothHeadset> clazz = mBluetoothHeadset.getClass();
		Method m1;
		if(BluetoothProfile.STATE_CONNECTED == mBluetoothHeadset.getConnectionState(remotedevice)){
//			try{
//				m1 = clazz.getMethod("disconnect", BluetoothDevice.class);
//				m1.invoke(mBluetoothHeadset, remotedevice);						
//			}catch(Exception e){
//				e.printStackTrace();
//			}
		}else{
			try{
				m1 = clazz.getMethod("connect", BluetoothDevice.class);
				m1.invoke(mBluetoothHeadset, remotedevice);						
			}catch(Exception e){
				e.printStackTrace();
			}
		}		
	}*/
    }

}
