package com.winteralexander.gdx.utils.math;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.winteralexander.gdx.utils.Validation;
import com.winteralexander.gdx.utils.io.Serializable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import static com.winteralexander.gdx.utils.io.StreamUtil.*;

/**
 * Represents a trail, a series of points in 2D space. The start of the trail
 * is assumed to be the zero vector and destinations are relative to it.
 * <p>
 * Created on 2019-05-10.
 *
 * @author Alexander Winter
 */
public class Trail implements Serializable {
	public final Array<Vector2> destinations = new Array<>();
	public boolean cycle = false;

	@Override
	public void readFrom(InputStream input) throws IOException {
		destinations.clear();
		cycle = readBoolean(input);
		int size = readShort(input);
		destinations.ensureCapacity(size);

		for(int i = 0; i < size; i++)
			destinations.add(new Vector2(readFloat(input), readFloat(input)));
	}

	@Override
	public void writeTo(OutputStream output) throws IOException {
		writeBoolean(output, cycle);
		writeShort(output, destinations.size);
		for(Vector2 dest : destinations) {
			writeFloat(output, dest.x);
			writeFloat(output, dest.y);
		}
	}

	public void set(Trail trail) {
		Validation.ensureNotNull(trail, "trail");
		destinations.clear();
		destinations.ensureCapacity(trail.destinations.size);

		for(Vector2 vec : trail.destinations)
			destinations.add(vec.cpy());

		cycle = trail.cycle;
		update();
	}

	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null || getClass() != o.getClass())
			return false;

		Trail trail = (Trail)o;
		return cycle == trail.cycle && Objects.equals(destinations, trail.destinations);
	}

	@Override
	public int hashCode() {
		return Objects.hash(destinations, cycle);
	}

	public Trail cpy() {
		Trail trail = new Trail();
		trail.set(this);
		return trail;
	}

	public void update() {}
}
