package org.silnith.browser.organic.parser;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageReader;
import javax.imageio.stream.FileCacheImageInputStream;
import javax.imageio.stream.ImageInputStream;

import org.silnith.browser.organic.network.Download;

public class ImageParser<T> implements FileParser<BufferedImage> {

	private final ImageReader imageReader;

	public ImageParser(final ImageReader imageReader) {
		super();
		this.imageReader = imageReader;
	}

	@Override
	public BufferedImage parse(final Download download) throws IOException {
		final ImageInputStream imageInputStream = new FileCacheImageInputStream(download.getInputStream(), null);
		imageReader.setInput(imageInputStream);
		final BufferedImage image = imageReader.read(0);
		return image;
	}

	public void dispose() {
		imageReader.dispose();
	}

}
