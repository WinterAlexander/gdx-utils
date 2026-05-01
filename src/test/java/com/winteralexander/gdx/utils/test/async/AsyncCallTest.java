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
		MutableBox<Boolean> conditionFlag = new MutableBox<>(true);
		MutableBox<String> result = new MutableBox<>();

		async((AsyncCall.CheckedVoidFunction<Long>)Thread::sleep, 10L)
				.when(() -> conditionFlag.get() == true)
				.then(v -> result.set("A"))
				.always(() -> result.set(result.get() + "B"))
				.execute()
				.join();

		assertEquals("AB", result.get());

		conditionFlag.set(false);

		async((AsyncCall.CheckedVoidFunction<Long>)Thread::sleep, 10L)
				.when(() -> conditionFlag.get() == true)
				.then(v -> fail(".when() should protect against this call"))
				.execute()
				.join();

		async((AsyncCall.CheckedVoidFunction<Long>)(value -> {
			throw new RuntimeException("Fail");
		}), 10L)
				.when(() -> conditionFlag.get() == true)
				.then(v -> fail("Function that always throws should never enter .then()"))
				.except(RuntimeException.class,
						ex -> fail(".when() should protect against this call"))
				.always(() -> fail(".when() should protect against this call"))
				.execute()
				.join();
	}
}
