package me.winter.gdx.utils.json;

import static me.winter.gdx.utils.StringUtil.quote;
import static me.winter.gdx.utils.Validation.ensureNotNull;

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
		ensureNotNull(name, "name");
		this.name = name;
	}

	@Override
	public String toJson(T object) {
		return quote(name) + ":" + quote(String.valueOf(object));
	}
}
