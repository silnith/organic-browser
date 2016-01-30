package org.silnith.browser.organic.network;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class Download {
    
    private final URL url;
    
    private URLConnection connection;
    
    private long contentLength;
    
    private String contentType;
    
    private String contentEncoding;
    
    private byte[] contentBytes;
    
    private String content;
    
    public Download(final URL url) {
        super();
        this.url = url;
        
        this.connection = null;
        this.contentLength = 0;
        this.contentType = null;
        this.contentEncoding = null;
        this.contentBytes = null;
        this.content = null;
    }
    
    public void connect() throws IOException {
        connection = url.openConnection();
        contentLength = connection.getContentLengthLong();
        contentType = connection.getContentType();
        contentEncoding = connection.getContentEncoding();
    }
    
    public void download() throws IOException {
        final InputStream inputStream = connection.getInputStream();
        final List<Byte> byteList = new ArrayList<>();
        int byteRead = inputStream.read();
        while (byteRead != -1) {
            byteList.add((byte) byteRead);
            byteRead = inputStream.read();
        }
        inputStream.close();
        
        contentBytes = new byte[byteList.size()];
        int i = 0;
        for (final byte b : byteList) {
            contentBytes[i++ ] = b;
        }
        if (contentEncoding == null) {
            contentEncoding = "UTF-8";
        }
        content = new String(contentBytes, contentEncoding);
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public String getContentEncoding() {
        return contentEncoding;
    }
    
    public long getContentLength() {
        return contentLength;
    }
    
    public byte[] getContentBytes() {
        return contentBytes;
    }
    
    public String getContent() {
        return content;
    }
    
    public InputStream getInputStream() {
        return new ByteArrayInputStream(contentBytes);
    }
    
}
