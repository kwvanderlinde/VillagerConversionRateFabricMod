package com.kwvanderlinde.fabricmc.villagerconversionrate.common.api.ui;

import java.util.function.Consumer;

public interface MenuBuilder {
	/**
	 * Sets the translation key to use for the title.
	 *
	 * @param titleTranslationKey
	 *      The translation key to look up to determine the title.
	 * @return
	 *      `this`
	 */
	MenuBuilder setTitle(String titleTranslationKey);

	/**
	 * Add a callback that should be called to save the configuration set in the menu.
	 *
	 * @param callback
	 *      A callback that performs the actions necessary to save the menu configuration.
	 * @return
	 *      `this`
	 */
	MenuBuilder onSave(Runnable callback);

	/**
	 * Create a new category, setting it as the current category.
	 *
	 * TODO Make this optional only if category headers are desired.
	 *
	 * @param categoryNameTranslationKey
	 *      The translation key to look up to determine the title of the category.
	 * @return
	 *      `this`
	 */
	MenuBuilder newCategory(String categoryNameTranslationKey,
	                        Consumer<CategoryBuilder> callback);
}
