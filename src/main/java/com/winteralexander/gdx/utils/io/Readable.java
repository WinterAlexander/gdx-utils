package com.winteralexander.gdx.utils.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Object that can be read from an {@link InputStream}
 * <p>
 * Created on 2023-08-22.
 *
 * @author Alexander Winter
 */
public interface Readable {
	/**
	 * Reads the input stream to fill this object's content
	 *
	 * @param input stream to read from
	 * @throws IOException if an IO error occurs during read
	 */
	void readFrom(InputStream input) throws IOException;
}
