package com.winteralexander.gdx.utils.json;

import com.winteralexander.gdx.utils.StringUtil;
import com.winteralexander.gdx.utils.Validation;

/**
 * {@link JsonFieldSerializer} for a simple type like number, string or enum
 * where the conversion to JSON of the value is a simple Object#toString() call
 * <p>
 * Created on 2021-11-24.
 *
 * @author Alexander Winter
 */
public class StringJsonFieldSerializer<T> implements JsonFieldSerializer<T> {
	private final String name;

	public StringJsonFieldSerializer(String name) {
		Validation.ensureNotNull(name, "name");
		this.name = name;
	}

	@Override
	public String toJson(T object) {
		return StringUtil.quote(name) + ":" + StringUtil.quote(String.valueOf(object));
	}
}
