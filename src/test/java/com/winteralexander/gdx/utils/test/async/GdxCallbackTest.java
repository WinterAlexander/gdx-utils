package com.winteralexander.gdx.utils.test.async;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.winteralexander.gdx.utils.async.GdxCallback;
import com.winteralexander.gdx.utils.property.MutableBox;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link GdxCallback}
 * <p>
 * Created on 2026-11-18.
 *
 * @author Alexander Winter
 */
public class GdxCallbackTest {
	@Test
	public void testBasicCallback() {
		MutableBox<Integer> box = new MutableBox<>();
		GdxCallback<Integer> cb = box::set;
		Gdx.app = Mockito.mock();
		Mockito.doAnswer(invocation -> {
				   Runnable runnable = invocation.getArgument(0);
				   runnable.run();
				   return null;
			   })
				.when(Gdx.app)
				.postRunnable(ArgumentMatchers.any(Runnable.class));

		cb.accept(5);
		assertEquals(5, box.get().intValue());

		Gdx.app = Mockito.mock();

		cb.accept(10);
		assertEquals(5, box.get().intValue());
	}
}
