package com.winteralexander.gdx.utils.test;

import com.winteralexander.gdx.utils.ConsumerUtil;
import org.junit.Test;

import java.util.function.Consumer;

import static org.junit.Assert.assertSame;

/**
 * Unit test for {@link ConsumerUtil}
 * <p>
 * Created on 2024-07-21.
 *
 * @author Alexander Winter
 */
public class ConsumerUtilTest {
	@Test
	public void testNoopConsumer() {
		Consumer<ConsumerUtilTest> consumer = ConsumerUtil.noopConsumer();
		consumer.accept(this);
		assertSame(consumer, ConsumerUtil.noopConsumer());
	}
}
