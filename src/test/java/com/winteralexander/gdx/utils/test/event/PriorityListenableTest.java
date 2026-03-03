package com.winteralexander.gdx.utils.test.event;

import com.badlogic.gdx.utils.IntArray;
import com.winteralexander.gdx.utils.EnumConstantCache;
import com.winteralexander.gdx.utils.event.PriorityListenable;
import com.winteralexander.gdx.utils.event.PriorityListenableImpl;
import org.junit.Test;

import java.util.function.BooleanSupplier;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link PriorityListenable}
 * <p>
 * Created on 2026-02-11.
 *
 * @author Alexander Winter
 */
public class PriorityListenableTest {
	private enum Priority {
		HIGH, MED, LOW;
		public static final Priority[] values = EnumConstantCache.store(values());
	}

	@Test
	public void testSimpleInvocation() {
		PriorityListenableImpl<BooleanSupplier, Priority> listenable =
				new PriorityListenableImpl<>(Priority.MED);
		IntArray array = new IntArray();
		listenable.addListener(() -> {
			array.add(1);
			return false;
		});
		listenable.addListener(() -> {
			array.add(2);
			return false;
		}, Priority.LOW);
		listenable.addListener(() -> {
			array.add(3);
			return false;
		}, Priority.HIGH);

		boolean handled = listenable.trigger(BooleanSupplier::getAsBoolean);
		assertFalse("Event must be handled", handled);
		assertEquals("Not all listeners were called", 3, array.size);
		assertEquals("Incorrect listener execution order", 3, array.get(0));
		assertEquals("Incorrect listener execution order", 1, array.get(1));
		assertEquals("Incorrect listener execution order", 2, array.get(2));
	}

	@Test
	public void testHandleEvent() {
		PriorityListenableImpl<BooleanSupplier, Priority> listenable =
				new PriorityListenableImpl<>(Priority.MED);
		IntArray array = new IntArray();
		listenable.addListener(() -> {
			array.add(1);
			return true;
		});
		listenable.addListener(() -> {
			array.add(2);
			return false;
		}, Priority.LOW);
		listenable.addListener(() -> {
			array.add(3);
			return false;
		}, Priority.HIGH);

		boolean handled = listenable.trigger(BooleanSupplier::getAsBoolean);
		assertTrue("Event must be handled", handled);
		assertEquals("Not all listeners were called", 2, array.size);
		assertEquals("Incorrect listener execution order", 3, array.get(0));
		assertEquals("Incorrect listener execution order", 1, array.get(1));
	}
}
