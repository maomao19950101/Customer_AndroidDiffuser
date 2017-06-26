package cn.mb.http;

import java.io.Serializable;

public class RequestResult implements Serializable{

	public static final class RequestResultStatus {
		 public static final int REQUEST_SUCCESS=1;
		 public static final int REQUEST_FAIL=0;
	}
	
	private static final long serialVersionUID = 205095584884622888L;
	private int status;
	private String message;
	private String responseData;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getResponseData() {
		return responseData;
	}
	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}
	
}