package com.winteralexander.gdx.utils.test;

import com.winteralexander.gdx.utils.Benchmark;
import org.junit.Test;

/**
 * Unit test for {@link BenchmarkTest}
 * <p>
 * Created on 2024-10-23.
 *
 * @author Alexander Winter
 */
@SuppressWarnings("deprecation")
public class BenchmarkTest {
	@Test
	public void benchmarkTest() throws InterruptedException {
		Benchmark.start();

		for(int i = 0; i < 1_000; i++)
			Thread.sleep(1L);

		Benchmark.step("Step1");

		for(int i = 0; i < 1_000; i++)
			Thread.sleep(1L);

		Benchmark.end("End");
	}
}
