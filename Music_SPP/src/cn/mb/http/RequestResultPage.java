package cn.mb.http;

public class RequestResultPage extends RequestResult {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7826778755572613719L;

	private Integer totalPage;
	private Integer totalCount;

	

	public Integer getTotalPage() {
		if(this.totalPage==null)return 0;
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getTotalCount() {
		if(this.totalCount==null)return 0;
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	
	
}
