package org.silnith.browser.organic.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadManager {

	public DownloadManager() {
		super();
	}

	public Download download(final URL url) throws IOException {
		final Download download = new Download(url);
		download.connect();
		download.download();
		return download;
	}

	public InputStream getInputStream(final URL url) throws IOException {
		final URLConnection connection = url.openConnection();
		System.out.println(connection.getHeaderFields());
		final String contentType = connection.getHeaderField("Content-Type");
		return connection.getInputStream();
	}

}
