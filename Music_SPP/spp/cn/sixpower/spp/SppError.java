package cn.sixpower.spp;



public enum SppError {
	NOT_SUPPORT_BLUETOOTH(101, "不支持蓝牙"),

	NOT_SUPPORT_BLE(102, "不支持ble"),

	BLUETOOTH_IS_DISABLE(103, "蓝牙未打开"),

	STATE_DISCONNECT(104, "设备已经断开了连接"),

	STATE_CONNECTING(105, "设备正在连接中"),

	STATE_SERVICE_DISCOVERY(106, "发现了设备服务"),

	STATE_CONNECTED(107, "设备已经连接上"),

	STATE_CONNECTED_AND_READY(108, "设备已经准备好，可以通讯了"),

	STATE_DISCONNECTING(109, "设备正在断开中"),

	SERVICE_DISCOVERY_NOT_STARTED(261, "gatt失败，无法发现服务"),

	SERVICE_NOT_FOUND(262, "获取设备服务 , 结果为null"),

	CHARACTERISTICS_NOT_FOUND(263, "获取特征失败，结果为null"),

	CONNECT_TIME_OUT(264, "连接超时"),
	
	CONNECT_FAILED(361, "Unable to connect device");
//	CONNECT_FAILED(361, "Unable to connect device");
	
	/*NOT_SUPPORT_BLUETOOTH(101, R.string.ble_error_101),

	NOT_SUPPORT_BLE(102, R.string.ble_error_102),

	BLUETOOTH_IS_DISABLE(103, R.string.ble_error_103),

	STATE_DISCONNECT(104, R.string.ble_error_104),

	STATE_CONNECTING(105, R.string.ble_error_105),

	STATE_SERVICE_DISCOVERY(106, R.string.ble_error_106),

	STATE_CONNECTED(107, R.string.ble_error_107),

	STATE_CONNECTED_AND_READY(108,R.string.ble_error_108),

	STATE_DISCONNECTING(109, R.string.ble_error_109),

	SERVICE_DISCOVERY_NOT_STARTED(261, R.string.ble_error_261),

	SERVICE_NOT_FOUND(262, R.string.ble_error_262),

	CHARACTERISTICS_NOT_FOUND(263, R.string.ble_error_263),

	CONNECT_TIME_OUT(264, R.string.ble_error_264); */

	private int errorCode;
	private String errorMsg;

	private SppError(int errorCode, String errorMsg) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	/*private BleError(int errorCode, int errorMsgResId) {
		this.errorCode = errorCode;
		this.errorMsg = BleAppApplication.getBleApplication().getResources().getString(errorMsgResId);
	}*/

	
	
	public int getErrorCode() {
		return this.errorCode;
	}

	public String getErrorMsg() {
		return this.errorMsg;
	}

	public static String getErrorMsgByCode(int errorCode) {
		for (SppError lm : values()) {
			if (lm.errorCode == errorCode)
				return lm.errorMsg;
		}
		return "连接失败";
		//return BleAppApplication.getBleApplication().getResources().getString(R.string.ble_error_unKnow);
	}
}
