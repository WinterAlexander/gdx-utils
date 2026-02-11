package com.winteralexander.gdx.utils.test.event;

import com.winteralexander.gdx.utils.event.Listenable;
import com.winteralexander.gdx.utils.event.ListenableImpl;
import com.winteralexander.gdx.utils.property.MutableBox;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the basic listenable functionality, which includes {@link Listenable} and
 * {@link ListenableImpl}
 * <p>
 * Created on 2026-02-10.
 *
 * @author Alexander Winter
 */
public class ListenableTest {
	@Test
	public void testSimpleInvocation() {
		ListenableImpl<Runnable> listenable = new ListenableImpl<>();
		MutableBox<Boolean> hasRun = new MutableBox<>(false);
		listenable.addListener(() -> {
			hasRun.set(true);
		});

		listenable.trigger(Runnable::run);
		assertTrue("Listenable did not run", hasRun.get());
	}

	@Test
	public void testRemoveWhileInvoking() {
		ListenableImpl<Runnable> listenable = new ListenableImpl<>();
		MutableBox<Integer> value = new MutableBox<>(0);

		Runnable secondListener = () -> {
			value.set(10);
		};
		listenable.addListener(() -> {
			value.set(1);
			listenable.removeListener(secondListener);
			assertTrue("Listenable must still have listener active",
					listenable.hasListener(secondListener));
		});
		listenable.addListener(secondListener);

		listenable.trigger(Runnable::run);
		assertEquals("Value must be updated by the second listener as removal while invocation " +
				"should be delayed until end of invocation", 10, (int)value.get());
		assertFalse("Listenable must no longer have listener active",
				listenable.hasListener(secondListener));
	}

	@Test
	public void testAddWhileInvoking() {

		ListenableImpl<Runnable> listenable = new ListenableImpl<>();
		MutableBox<Integer> value = new MutableBox<>(0);

		Runnable secondListener = () -> {
			value.set(10);
		};
		listenable.addListener(() -> {
			value.set(1);
			listenable.addListener(secondListener);
			assertFalse("Listenable must not have the second listener added yet",
					listenable.hasListener(secondListener));
		});

		listenable.trigger(Runnable::run);
		assertEquals("Value must not be updated by the second listener as add while invocation " +
				"should be delayed until end of invocation", 1, (int)value.get());
		assertTrue("Listenable must now have listener active",
				listenable.hasListener(secondListener));
	}
}
