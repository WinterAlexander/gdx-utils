package me.winter.gdx.utils.io;

import java.io.IOException;

/**
 * Thrown when making an http request that resulted into an error (status code
 * not 200)
 * <p>
 * Created on 2021-07-22.
 *
 * @author Alexander Winter
 */
public class HttpRequestFailedException extends IOException {
	private final int responseCode;

	public HttpRequestFailedException(int responseCode) {
		this.responseCode = responseCode;
	}

	public HttpRequestFailedException(int responseCode, String message) {
		super(message);
		this.responseCode = responseCode;
	}

	public int getResponseCode() {
		return responseCode;
	}
}
