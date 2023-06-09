package me.winter.gdx.utils.property;

import me.winter.gdx.utils.Validation;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * {@link Property} implemented from a consumer and a supplier passed as
 * parameters
 * <p>
 * Created on 2021-09-19.
 *
 * @author Alexander Winter
 */
public class ConsumerSupplierProperty<T> implements Property<T> {
	private final Supplier<T> getter;
	private final Consumer<T> setter;

	public ConsumerSupplierProperty(Supplier<T> getter, Consumer<T> setter) {
		Validation.ensureNotNull(getter, "getter");
		Validation.ensureNotNull(setter, "setter");
		this.getter = getter;
		this.setter = setter;
	}

	@Override
	public void set(T t) {
		setter.accept(t);
	}

	@Override
	public T get() {
		return getter.get();
	}
}
