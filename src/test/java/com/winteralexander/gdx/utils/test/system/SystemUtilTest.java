package com.winteralexander.gdx.utils.test.system;

import com.winteralexander.gdx.utils.ReflectionUtil;
import com.winteralexander.gdx.utils.StringUtil;
import com.winteralexander.gdx.utils.system.SystemUtil;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for {@link SystemUtil}
 * <p>
 * Created on 2025-03-07.
 *
 * @author Alexander Winter
 */
public class SystemUtilTest {
	@Test
	public void testSystemSpecs() throws Exception {
		String[] cpus = SystemUtil.getCPUs();
		assertTrue("Number of CPU must be greater than 0", cpus.length > 0);

		String[] gpus = SystemUtil.getGPUs();

		SystemUtil.SystemMemory mem = SystemUtil.getRAM();
		assertTrue("Total memory must be greater than 0", mem.total > 0L);
		assertTrue("Available memory must be greater than 0", mem.available > 0L);
		assertTrue("Free memory must be greater than 0", mem.free > 0L);

		System.out.println("CPUs: " + StringUtil.join(cpus, ", "));
		System.out.println("GPUs: " + StringUtil.join(gpus, ", "));
		System.out.println("Memory: " + mem.total + "b total, " + mem.free + "b free, " + mem.available + "b available");
	}
}
