package com.winteralexander.gdx.utils.test.event;

import com.winteralexander.gdx.utils.event.InheritanceListenable;
import com.winteralexander.gdx.utils.property.MutableBox;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for {@link InheritanceListenable}
 * <p>
 * Created on 2024-07-31.
 *
 * @author Alexander Winter
 */
public class InheritanceListenableTest {
	@Test
	public void testBasic() {
		InheritanceListenable<BaseListener, ChildListener> inheritanceListenable =
				new InheritanceListenable<BaseListener, ChildListener>(ChildListener.class) {
			@Override
			protected ChildListener wrapListener(BaseListener listener) {
				return new ChildListener() {
					@Override
					public void addedThing(Object thing) {}

					@Override
					public void changed() {
						listener.changed();
					}
				};
			}
		};

		MutableBox<Integer> changedCount = new MutableBox<>(0);
		MutableBox<Integer> addedCount = new MutableBox<>(0);

		BaseListener baseListener;
		ChildListener childListener;

		inheritanceListenable.addListener(baseListener =
				() -> changedCount.set(changedCount.get() + 1));

		inheritanceListenable.addListener(childListener = new ChildListener() {
			@Override
			public void addedThing(Object thing) {
				addedCount.set(addedCount.get() + 1);
			}

			@Override
			public void changed() {
				changedCount.set(changedCount.get() + 1);
			}
		});

		inheritanceListenable.trigger(l -> l.addedThing(Boolean.TRUE));
		inheritanceListenable.trigger(BaseListener::changed);

		assertEquals(1, (int)addedCount.get());
		assertEquals(2, (int)changedCount.get());

		inheritanceListenable.removeListener(baseListener);

		inheritanceListenable.trigger(l -> l.addedThing(Boolean.TRUE));
		inheritanceListenable.trigger(BaseListener::changed);

		assertEquals(2, (int)addedCount.get());
		assertEquals(3, (int)changedCount.get());
	}

	private interface BaseListener {
		void changed();
	}

	private interface ChildListener extends BaseListener {
		void addedThing(Object thing);
	}
}
