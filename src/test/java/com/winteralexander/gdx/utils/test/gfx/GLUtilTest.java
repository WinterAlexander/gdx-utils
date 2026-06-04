package com.winteralexander.gdx.utils.test.gfx;

import com.winteralexander.gdx.utils.test.g3d.MockGL;
import com.winteralexander.gdx.utils.gfx.GLUtil;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit test for {@link GLUtil} (requires GPU)
 * <p>
 * Created on 2026-06-04.
 *
 * @author Alexander Winter
 */
@Ignore
public class GLUtilTest {
	@BeforeClass
	public static void initGL() {
		MockGL.init();
	}

	@Test
	public void testGetValues() {
		System.out.println("getMaxTextureImageUnits(): " + GLUtil.getMaxTextureImageUnits());
		System.out.println("getMaxTextureSize(): " + GLUtil.getMaxTextureSize());
		System.out.println("getMaxVertexUniformVectors(): " + GLUtil.getMaxVertexUniformVectors());
		System.out.println("getMinSmoothLineWidth(): " + GLUtil.getMinSmoothLineWidth());
		System.out.println("getMaxSmoothLineWidth(): " + GLUtil.getMaxSmoothLineWidth());
		System.out.println("getSmoothLineGranularity(): " + GLUtil.getSmoothLineGranularity());
		System.out.println("getMinAliasedLineWidth(): " + GLUtil.getMinAliasedLineWidth());
		System.out.println("getMaxAliasedLineWidth(): " + GLUtil.getMaxAliasedLineWidth());
	}
}
