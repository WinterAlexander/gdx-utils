package com.winteralexander.gdx.utils.json;

/**
 * Serializes a specified object into Json for a given type, including the Json
 * field name and value.
 * e.g. "name": "value"
 * <p>
 * Created on 2021-11-24.
 *
 * @author Alexander Winter
 */
public interface JsonFieldSerializer<T> {
	String toJson(T object);
}
