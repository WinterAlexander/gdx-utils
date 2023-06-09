package me.winter.gdx.utils.i18n;

import com.badlogic.gdx.utils.I18NBundle;

/**
 * {@link Localizable} which has an associated localization key
 * <p>
 * Created on 2021-10-13.
 *
 * @author Alexander Winter
 */
@FunctionalInterface
public interface SingleKeyLocalizable extends Localizable {
	String getLangKey();

	@Override
	default String getTranslation(I18NBundle lang) {
		return lang.get(getLangKey());
	}
}
