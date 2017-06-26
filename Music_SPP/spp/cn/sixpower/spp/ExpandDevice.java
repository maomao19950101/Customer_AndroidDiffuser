package cn.sixpower.spp;

import java.io.Serializable;

import android.bluetooth.BluetoothDevice;

public class ExpandDevice implements Serializable{
	private static final long serialVersionUID = -1604714575452105679L;
	private String macAddress;
	private String deviceName;
	private int rssi;
private BluetoothDevice device;
	public ExpandDevice() {
	}

	public ExpandDevice(String macAddress, String deviceName, int rssi,BluetoothDevice device) {
		this.macAddress = macAddress;
		this.deviceName = deviceName;
		this.rssi = rssi;
		this.device = device;
	}

	public String getMacAddress() {
		return this.macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getDeviceName() {
		return this.deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public int getRssi() {
		return this.rssi;
	}

	public void setRssi(int rssi) {
		this.rssi = rssi;
	}

	public boolean equals(Object o) {
		if (!(o instanceof ExpandDevice))
			return false;
		ExpandDevice device = (ExpandDevice) o;
		return device.getMacAddress().equals(getMacAddress())&&device.getDeviceName().equals(getDeviceName());
	}

	public BluetoothDevice getDevice() {
		return device;
	}

	public void setDevice(BluetoothDevice device) {
		this.device = device;
	}
	
	
}
