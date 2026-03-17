package com.winteralexander.gdx.utils.test.function;

import com.winteralexander.gdx.utils.function.FunctionUtil;
import org.junit.Test;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
import static org.junit.Assert.*;

/**
 * Unit test for {@link FunctionUtil}
 * <p>
 * Created on 2024-07-21.
 *
 * @author Alexander Winter
 */
public class FunctionUtilTest {
	@Test
	public void testNoopConsumer() {
		Consumer<FunctionUtilTest> consumer = FunctionUtil.noopConsumer();
		consumer.accept(this);
		assertSame(consumer, FunctionUtil.noopConsumer());
	}

	@Test
	public void testPredicateToBiPredicate() {
		BiPredicate<Integer, Integer> bi1 = FunctionUtil.testFirst(i -> i == 1);
		BiPredicate<Integer, Integer> bi2 = FunctionUtil.testSecond(i -> i == 1);
		assertTrue(bi1.test(1, 0));
		assertFalse(bi1.test(0, 1));
		assertTrue(bi2.test(0, 1));
		assertFalse(bi2.test(1, 0));
	}
}
