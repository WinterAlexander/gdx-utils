package com.winteralexander.gdx.utils.test.gfx;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.winteralexander.gdx.utils.gfx.ParticleUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests ParticleUtil
 * <p>
 * Created on 2018-02-13.
 *
 * @author Alexander Winter
 */
public class ParticleUtilTest {
	private static final float ERROR = 0.1f;

	@Test
	public void testSimpleRotation() {
		ParticleEffect effect = new ParticleEffect();

		ParticleEmitter emitter = new ParticleEmitter();

		emitter.getXOffsetValue().setActive(true);
		emitter.getXOffsetValue().setLow(50, 100);

		effect.getEmitters().add(emitter);

		ParticleUtil.rotate(effect, 90f);

		assertTrue(emitter.getXOffsetValue().isActive());
		assertTrue(emitter.getYOffsetValue().isActive());

		assertEquals(0, emitter.getXOffsetValue().getLowMin(), ERROR);
		assertEquals(0, emitter.getXOffsetValue().getLowMax(), ERROR);
		assertEquals(50, emitter.getYOffsetValue().getLowMin(), ERROR);
		assertEquals(100, emitter.getYOffsetValue().getLowMax(), ERROR);

		assertEquals(90, emitter.getAngle().getHighMin(), ERROR);
		assertEquals(90, emitter.getAngle().getHighMax(), ERROR);
		assertEquals(90, emitter.getAngle().getLowMin(), ERROR);
		assertEquals(90, emitter.getAngle().getLowMax(), ERROR);
		assertEquals(90, emitter.getRotation().getLowMin(), ERROR);
		assertEquals(90, emitter.getRotation().getLowMax(), ERROR);
		assertEquals(90, emitter.getRotation().getHighMin(), ERROR);
		assertEquals(90, emitter.getRotation().getHighMax(), ERROR);

		ParticleUtil.rotate(effect, 90f);

		assertEquals(-50, emitter.getXOffsetValue().getLowMin(), ERROR);
		assertEquals(-100, emitter.getXOffsetValue().getLowMax(), ERROR);
		assertEquals(0, emitter.getYOffsetValue().getLowMin(), ERROR);
		assertEquals(0, emitter.getYOffsetValue().getLowMax(), ERROR);

		assertEquals(180, emitter.getAngle().getHighMin(), ERROR);
		assertEquals(180, emitter.getAngle().getHighMax(), ERROR);
		assertEquals(180, emitter.getAngle().getLowMin(), ERROR);
		assertEquals(180, emitter.getAngle().getLowMax(), ERROR);
		assertEquals(180, emitter.getRotation().getLowMin(), ERROR);
		assertEquals(180, emitter.getRotation().getLowMax(), ERROR);
		assertEquals(180, emitter.getRotation().getHighMin(), ERROR);
		assertEquals(180, emitter.getRotation().getHighMax(), ERROR);

		ParticleUtil.setRotation(effect, 90f);

		assertEquals(0, emitter.getXOffsetValue().getLowMin(), ERROR);
		assertEquals(0, emitter.getXOffsetValue().getLowMax(), ERROR);
		assertEquals(50, emitter.getYOffsetValue().getLowMin(), ERROR);
		assertEquals(100, emitter.getYOffsetValue().getLowMax(), ERROR);

		assertEquals(90, emitter.getAngle().getHighMin(), ERROR);
		assertEquals(90, emitter.getAngle().getHighMax(), ERROR);
		assertEquals(90, emitter.getAngle().getLowMin(), ERROR);
		assertEquals(90, emitter.getAngle().getLowMax(), ERROR);
		assertEquals(90, emitter.getRotation().getLowMin(), ERROR);
		assertEquals(90, emitter.getRotation().getLowMax(), ERROR);
		assertEquals(90, emitter.getRotation().getHighMin(), ERROR);
		assertEquals(90, emitter.getRotation().getHighMax(), ERROR);
	}

	@Test
	public void testZeroRotation() {
		ParticleEffect effect = new ParticleEffect();

		ParticleEmitter emitter = new ParticleEmitter();

		emitter.getXOffsetValue().setActive(true);
		emitter.getXOffsetValue().setLow(50, 100);

		emitter.getYOffsetValue().setActive(true);
		emitter.getYOffsetValue().setLow(36, 44);

		effect.getEmitters().add(emitter);

		ParticleUtil.rotate(effect, 45f);
		ParticleUtil.rotate(effect, 60f);
		ParticleUtil.rotate(effect, 70f);
		ParticleUtil.setRotation(effect, 0f);
		ParticleUtil.rotate(effect, 90f);
		ParticleUtil.rotate(effect, -90f);
		ParticleUtil.rotate(effect, 0f);

		assertTrue(emitter.getXOffsetValue().isActive());
		assertTrue(emitter.getYOffsetValue().isActive());

		assertEquals(50, emitter.getXOffsetValue().getLowMin(), ERROR);
		assertEquals(100, emitter.getXOffsetValue().getLowMax(), ERROR);
		assertEquals(36, emitter.getYOffsetValue().getLowMin(), ERROR);
		assertEquals(44, emitter.getYOffsetValue().getLowMax(), ERROR);

		assertEquals(0, emitter.getAngle().getHighMin(), ERROR);
		assertEquals(0, emitter.getAngle().getHighMax(), ERROR);
		assertEquals(0, emitter.getAngle().getLowMin(), ERROR);
		assertEquals(0, emitter.getAngle().getLowMax(), ERROR);
		assertEquals(0, emitter.getRotation().getLowMin(), ERROR);
		assertEquals(0, emitter.getRotation().getLowMax(), ERROR);
		assertEquals(0, emitter.getRotation().getHighMin(), ERROR);
		assertEquals(0, emitter.getRotation().getHighMax(), ERROR);
	}
}
