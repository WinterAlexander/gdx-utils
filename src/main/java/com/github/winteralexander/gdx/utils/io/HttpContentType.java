package com.github.winteralexander.gdx.utils.io;

import static com.github.winteralexander.gdx.utils.Validation.ensureNotNull;

/**
 * Type of content to be sent as part of an HTTP Post request
 * <p>
 * Created on 2022-08-09.
 *
 * @author Alexander Winter
 */
public enum HttpContentType {
	JSON("application/json"),
	X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
	;

	private final String value;

	HttpContentType(String value) {
		ensureNotNull(value, "value");
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return getValue();
	}
}
