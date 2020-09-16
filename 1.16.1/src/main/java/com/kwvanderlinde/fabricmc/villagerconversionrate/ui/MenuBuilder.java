package com.kwvanderlinde.fabricmc.villagerconversionrate.ui;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MenuBuilder implements com.kwvanderlinde.fabricmc.villagerconversionrate.common.api.ui.MenuBuilder {
	private final ConfigBuilder configBuilder;
	private final List<Runnable> onSaveCallbacks;

	public MenuBuilder(ConfigBuilder configBuilder) {
		this.configBuilder = configBuilder;
		this.onSaveCallbacks = new ArrayList<>();
	}

	public List<Runnable> getOnSaveCallbacks() {
		return onSaveCallbacks;
	}

	@Override
	public com.kwvanderlinde.fabricmc.villagerconversionrate.common.api.ui.MenuBuilder setTitle(String titleTranslationKey) {
		this.configBuilder.setTitle(new TranslatableText(titleTranslationKey));
		return this;
	}

	@Override
	public com.kwvanderlinde.fabricmc.villagerconversionrate.common.api.ui.MenuBuilder onSave(Runnable callback) {
		this.onSaveCallbacks.add(callback);
		return this;
	}

	@Override
	public com.kwvanderlinde.fabricmc.villagerconversionrate.common.api.ui.MenuBuilder newCategory(String categoryNameTranslationKey, Consumer<com.kwvanderlinde.fabricmc.villagerconversionrate.common.api.ui.CategoryBuilder> callback) {
		ConfigCategory category = configBuilder.getOrCreateCategory(new TranslatableText(categoryNameTranslationKey));
		CategoryBuilder categoryBuilder = new CategoryBuilder(category, configBuilder.entryBuilder());
		callback.accept(categoryBuilder);
		return this;
	}
}
