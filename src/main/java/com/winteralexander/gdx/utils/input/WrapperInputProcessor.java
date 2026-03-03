package com.winteralexander.gdx.utils.input;

import com.badlogic.gdx.InputProcessor;

/**
 * An {@link InputProcessor} which's behavior is defined by another inner input processor. Useful
 * to override the behavior of an input processor by intercepting its events
 * <p>
 * Created on 2026-03-01.
 *
 * @author Alexander Winter
 */
public interface WrapperInputProcessor extends InputProcessor {
	InputProcessor getInnerProcessor();

	@Override
	default boolean keyDown(int keycode) {
		return getInnerProcessor().keyDown(keycode);
	}

	@Override
	default boolean keyUp(int keycode) {
		return getInnerProcessor().keyUp(keycode);
	}

	@Override
	default boolean keyTyped(char character) {
		return getInnerProcessor().keyTyped(character);
	}

	@Override
	default boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return getInnerProcessor().touchDown(screenX, screenY, pointer, button);
	}

	@Override
	default boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return getInnerProcessor().touchUp(screenX, screenY, pointer, button);
	}

	@Override
	default boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		return getInnerProcessor().touchCancelled(screenX, screenY, pointer, button);
	}

	@Override
	default boolean touchDragged(int screenX, int screenY, int pointer) {
		return getInnerProcessor().touchDragged(screenX, screenY, pointer);
	}

	@Override
	default boolean mouseMoved(int screenX, int screenY) {
		return getInnerProcessor().mouseMoved(screenX, screenY);
	}

	@Override
	default boolean scrolled(float amountX, float amountY) {
		return getInnerProcessor().scrolled(amountX, amountY);
	}
}
