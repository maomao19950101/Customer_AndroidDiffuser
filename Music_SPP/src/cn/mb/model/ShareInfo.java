package cn.mb.model;

import java.io.Serializable;

public class ShareInfo implements Serializable {

	public static final String web_site="http://www.lcpower.cn";
	public static final String web_name="ShareName";
	private static final long serialVersionUID = 1L;

	private String shareUrl=web_site;
	private String text;
	private String imgUrl;
	private String siteUrl=web_site;
	private String title=web_name;

	public ShareInfo(String text, String imgUrl) {
		super();
		this.text = text;
		this.imgUrl = imgUrl;
	}
	public ShareInfo(String text, String imgUrl,String shareUrl) {
		super();
		this.text = text;
		this.imgUrl = imgUrl;
		this.shareUrl =shareUrl;
	}
	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	

	public String getSiteUrl() {
		return siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
