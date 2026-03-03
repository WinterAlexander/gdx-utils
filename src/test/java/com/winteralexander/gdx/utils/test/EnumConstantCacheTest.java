package com.winteralexander.gdx.utils.test;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.winteralexander.gdx.utils.EnumConstantCache;
import com.winteralexander.gdx.utils.ReflectionUtil;
import com.winteralexander.gdx.utils.gfx.UVTransform;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static com.winteralexander.gdx.utils.ReflectionUtil.getClasses;
import static com.winteralexander.gdx.utils.ReflectionUtil.loadClasses;
import static org.junit.Assert.*;

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

	enum ExtendedEnum {
		VALUE {
			@Override
			public String toString() {
				return "Value";
			}
		},
		VALUE2 {
			@Override
			public String toString() {
				return "Value2";
			}
		};

		public static final ExtendedEnum[] values = EnumConstantCache.store(values());
	}

	@Test
	public void testEnumExtension() {
		EnumConstantCache.store(ExtendedEnum.values());
		assertTrue(EnumConstantCache.isCached(ExtendedEnum.class));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testAllEnumsCached() throws ClassNotFoundException {
		Array<String> paths = ReflectionUtil.scanClasspath();
		Array<String> classNames = getClasses(paths);
		Array<Class<?>> classes = loadClasses(classNames
				.select(c -> c.startsWith("com.winteralexander.gdx.utils")));

		boolean fail = false;
		for(Class<?> type : classes) {
			if(type.isEnum() && !EnumConstantCache.isCached((Class<? extends Enum>)type)) {
				System.err.println(type + " is not cached using EnumConstantCache!");
				fail = true;
			}
		}
		if(fail)
			Assert.fail("At least one enum is not properly cached");
	}
}
