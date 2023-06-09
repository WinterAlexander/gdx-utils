package me.winter.gdx.utils;

/**
 * {@link FloatSupplier} which will always return the same value
 * <p>
 * Created on 2020-11-30.
 *
 * @author Alexander Winter
 */
public class ConstFloatSupplier implements FloatSupplier {
	public static final ConstFloatSupplier ZERO = new ConstFloatSupplier(0f);
	public static final ConstFloatSupplier ONE = new ConstFloatSupplier(1f);

	private final float value;

	public ConstFloatSupplier(float value) {
		this.value = value;
	}

	@Override
	public float getAsFloat() {
		return value;
	}
}
