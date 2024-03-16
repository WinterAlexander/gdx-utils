package com.winteralexander.gdx.utils.math.trail;

import com.badlogic.gdx.utils.Array;
import com.winteralexander.gdx.utils.Validation;
import com.winteralexander.gdx.utils.io.Serializable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import static com.winteralexander.gdx.utils.io.StreamUtil.*;

/**
 * Represents a trail, a series of points in space. The start of the trail
 * is assumed to be the zero vector and destinations are relative to it.
 * <p>
 * Created on 2019-05-10.
 *
 * @author Alexander Winter
 */
public abstract class Trail<T> implements Serializable {
	public final Array<T> destinations = new Array<>();
	public boolean cycle = false;

	@Override
	public void readFrom(InputStream input) throws IOException {
		destinations.clear();
		cycle = readBoolean(input);
		int size = readShort(input);
		destinations.ensureCapacity(size);

		for(int i = 0; i < size; i++)
			destinations.add(readElement(input));
	}

	@Override
	public void writeTo(OutputStream output) throws IOException {
		writeBoolean(output, cycle);
		writeShort(output, destinations.size);
		for(T dest : destinations)
			writeElement(output, dest);
	}

	protected abstract void writeElement(OutputStream output, T element) throws IOException;

	protected abstract T readElement(InputStream input) throws IOException;

	protected abstract T copyElement(T element);

	public abstract Trail<T> cpy();

	public void set(Trail<T> trail) {
		Validation.ensureNotNull(trail, "trail");
		destinations.clear();
		destinations.ensureCapacity(trail.destinations.size);

		for(T vec : trail.destinations)
			destinations.add(copyElement(vec));

		cycle = trail.cycle;
		update();
	}

	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null || getClass() != o.getClass())
			return false;

		Trail<?> trail = (Trail<?>)o;
		return cycle == trail.cycle && Objects.equals(destinations, trail.destinations);
	}

	@Override
	public int hashCode() {
		return Objects.hash(destinations, cycle);
	}

	public void update() {}
}
