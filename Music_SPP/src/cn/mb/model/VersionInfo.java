package cn.mb.model;

import java.io.Serializable;

public class VersionInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String Version;
	private String iOSUrl;
	private String AndroidUrl;
	public String getVersion() {
		return Version;
	}
	public void setVersion(String version) {
		Version = version;
	}
	public String getiOSUrl() {
		return iOSUrl;
	}
	public void setiOSUrl(String iOSUrl) {
		this.iOSUrl = iOSUrl;
	}
	public String getAndroidUrl() {
		return AndroidUrl;
	}
	public void setAndroidUrl(String androidUrl) {
		AndroidUrl = androidUrl;
	}
	
	
	
}
