package me.winter.gdx.utils.i18n;

import com.badlogic.gdx.utils.I18NBundle;

/**
 * Something which has a translation
 * <p>
 * Created on 2021-09-19.
 *
 * @author Alexander Winter
 */
@FunctionalInterface
public interface Localizable {
	String getTranslation(I18NBundle lang);
}
