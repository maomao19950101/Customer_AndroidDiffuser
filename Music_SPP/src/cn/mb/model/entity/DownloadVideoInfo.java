package cn.mb.model.entity;

import java.io.File;

import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;
import com.lidroid.xutils.http.HttpHandler;

import cn.mb.model.VideoInfo;

@Table(name="download_video")
public class DownloadVideoInfo extends VideoInfo{
	  /**
	 * 
	 */
	private static final long serialVersionUID = -8455800334479094337L;

	private long id;

	    @Transient
	    private HttpHandler<File> handler;

	    private HttpHandler.State state;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public HttpHandler<File> getHandler() {
			return handler;
		}

		public void setHandler(HttpHandler<File> handler) {
			this.handler = handler;
		}

		public HttpHandler.State getState() {
			return state;
		}

		public void setState(HttpHandler.State state) {
			this.state = state;
		}
	    
}
