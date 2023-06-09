package me.winter.gdx.utils.i18n;

import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

import static me.winter.gdx.utils.Validation.ensureNotNull;

/**
 * {@link I18NBundle} wrapper that fixes the non breaking space issue
 * <p>
 * Created on 2021-08-20.
 *
 * @author Alexander Winter
 */
public class I18NBundleSpaceFixWrapper extends I18NBundle {
	private final I18NBundle bundle;

	public I18NBundleSpaceFixWrapper(I18NBundle bundle) {
		ensureNotNull(bundle, "bundle");
		this.bundle = bundle;
	}

	@Override
	public Locale getLocale() {
		return bundle.getLocale();
	}

	@Override
	public String get(String key) {
		return bundle.get(key).replace('\u00A0', ' ');
	}

	@Override
	public String format(String key, Object... args) {
		return bundle.format(key, args).replace('\u00A0', ' ');
	}

	@Override
	public void debug(String placeholder) {
		bundle.debug(placeholder);
	}
}
