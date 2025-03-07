package com.winteralexander.gdx.utils.test.system;

import com.winteralexander.gdx.utils.ReflectionUtil;
import com.winteralexander.gdx.utils.system.SystemSpecs;
import com.winteralexander.gdx.utils.system.SystemUtil;
import org.junit.Test;

/**
 * Unit test for {@link SystemUtil}
 * <p>
 * Created on 2025-03-07.
 *
 * @author Alexander Winter
 */
public class SystemUtilTest {
	@Test
	public void testSystemSpecs() {
		SystemSpecs specs = SystemUtil.getSpecs();
		System.out.println(ReflectionUtil.toPrettyString(specs));
	}
}
