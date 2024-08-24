package com.winteralexander.gdx.utils.test;

import com.winteralexander.gdx.utils.EnumConstantCache;
import com.winteralexander.gdx.utils.gfx.UVTransform;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link EnumConstantCache}
 * <p>
 * Created on 2024-08-24.
 *
 * @author Alexander Winter
 */
public class EnumConstantCacheTest {
	@Test
	public void testCache() {
		UVTransform[] values = EnumConstantCache.get(UVTransform.class);
		UVTransform[] values2 = EnumConstantCache.get(UVTransform.class);
		assertSame(values, values2);
	}
}
