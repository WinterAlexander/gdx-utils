package com.winteralexander.gdx.utils.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Object that can be written to an {@link OutputStream}
 * <p>
 * Created on 2023-08-22.
 *
 * @author Alexander Winter
 */
public interface Writable {
	/**
	 * Writes the content of this object to the specified output stream
	 *
	 * @param output stream to write to
	 * @throws IOException if an IO error occurs
	 */
	void writeTo(OutputStream output) throws IOException;
}