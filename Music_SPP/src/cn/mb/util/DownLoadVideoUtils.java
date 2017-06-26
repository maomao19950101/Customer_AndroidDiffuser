package cn.mb.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Environment;
import cn.mb.app.AppAplication;

public class DownLoadVideoUtils {

	public void writeMedia(final String path) {
		AppAplication.pool.execute(new Runnable() {
			@Override
			public void run() {
				FileOutputStream out = null;
				InputStream is = null;
				String localUrl=null;
				long mediaLength = 0;
				long readSize = 0;
				try {
					URL url = new URL(path);
					HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();

					if (localUrl == null) {
						String videoUrl=path;
						String videoName=videoUrl.substring(videoUrl.lastIndexOf("/")+1);
						localUrl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/VideoCache/" + videoName;
					}

					File cacheFile = new File(localUrl);

					if (!cacheFile.exists()) {
						cacheFile.getParentFile().mkdirs();
						cacheFile.createNewFile();
					}

					readSize = cacheFile.length();
					out = new FileOutputStream(cacheFile, true);

					httpConnection.setRequestProperty("User-Agent", "NetFox");
					httpConnection.setRequestProperty("RANGE", "bytes="
							+ readSize + "-");

					is = httpConnection.getInputStream();

					mediaLength = httpConnection.getContentLength();
					if (mediaLength == -1) {
						return;
					}

					mediaLength += readSize;

					byte buf[] = new byte[4 * 1024];
					int size = 0;

					while ((size = is.read(buf)) != -1) {
						try {
							out.write(buf, 0, size);
							readSize += size;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (out != null) {
						try {
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

			}
		});
	}

}
