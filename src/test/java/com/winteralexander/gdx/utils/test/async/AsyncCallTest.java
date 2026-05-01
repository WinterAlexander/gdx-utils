package com.winteralexander.gdx.utils.test.async;

import com.winteralexander.gdx.utils.async.AsyncCall;
import com.winteralexander.gdx.utils.property.MutableBox;
import org.junit.Test;

import static com.winteralexander.gdx.utils.async.AsyncCall.async;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit test for {@link AsyncCall}
 * <p>
 * Created on 2026-05-01.
 *
 * @author Alexander Winter
 */
public class AsyncCallTest {
	@Test
	public void testWhen() {
		MutableBox<Boolean> box = new MutableBox<>(true);
		MutableBox<String> result = new MutableBox<>();
		async((AsyncCall.CheckedVoidFunction<Long>)Thread::sleep, 1000L)
		.when(() -> box.get() == true)
		.then(v -> {
			result.set("Success");
		})
		.execute();

		assertEquals("Success", result.get());

		box.set(false);

		async((AsyncCall.CheckedVoidFunction<Long>)Thread::sleep, 1000L)
		.when(() -> box.get() == true)
		.then(v -> {
			fail("When should protect this call");
		})
		.execute();

	}
}
