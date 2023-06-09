package me.winter.gdx.utils.json;

import com.badlogic.gdx.math.Vector2;

import static me.winter.gdx.utils.StringUtil.quote;
import static me.winter.gdx.utils.Validation.ensureNotNull;

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
		ensureNotNull(name, "name");
		this.name = name;
	}

	@Override
	public String toJson(Vector2 object) {
		return quote(name) + ":[" + object.x + "," + object.y + "]";
	}
}
