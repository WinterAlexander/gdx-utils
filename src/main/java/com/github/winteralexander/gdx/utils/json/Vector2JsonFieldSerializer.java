package com.github.winteralexander.gdx.utils.json;

import com.badlogic.gdx.math.Vector2;
import com.github.winteralexander.gdx.utils.StringUtil;
import com.github.winteralexander.gdx.utils.Validation;

/**
 * {@link JsonFieldSerializer} for a {@link Vector2}
 * <p>
 * Created on 2021-11-24.
 *
 * @author Alexander Winter
 */
public class Vector2JsonFieldSerializer implements JsonFieldSerializer<Vector2> {
	private final String name;

	public Vector2JsonFieldSerializer(String name) {
		Validation.ensureNotNull(name, "name");
		this.name = name;
	}

	@Override
	public String toJson(Vector2 object) {
		return StringUtil.quote(name) + ":[" + object.x + "," + object.y + "]";
	}
}
