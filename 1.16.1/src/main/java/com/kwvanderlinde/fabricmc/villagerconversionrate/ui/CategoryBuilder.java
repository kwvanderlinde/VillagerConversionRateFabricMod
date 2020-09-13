package com.kwvanderlinde.fabricmc.villagerconversionrate.ui;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.BooleanToggleBuilder;
import me.shedaniel.clothconfig2.impl.builders.LongSliderBuilder;
import net.minecraft.text.TranslatableText;

import java.util.function.Consumer;

public class CategoryBuilder implements com.kwvanderlinde.fabricmc.villagerconversionrate.common.api.ui.CategoryBuilder {
	private final ConfigCategory category;
	private final ConfigEntryBuilder entryBuilder;

	public CategoryBuilder(ConfigCategory category, ConfigEntryBuilder entryBuilder) {
		this.category = category;
		this.entryBuilder = entryBuilder;
	}

	public com.kwvanderlinde.fabricmc.villagerconversionrate.common.api.ui.CategoryBuilder addToggle(String labelTranslationKey, boolean initialValue, Consumer<com.kwvanderlinde.fabricmc.villagerconversionrate.common.api.ui.ToggleBuilder> callback) {
		BooleanToggleBuilder booleanToggleBuilder = entryBuilder.startBooleanToggle(new TranslatableText(labelTranslationKey), initialValue);
		callback.accept(new ToggleBuilder(booleanToggleBuilder));
		category.addEntry(booleanToggleBuilder.build());
		return this;
	}

	public com.kwvanderlinde.fabricmc.villagerconversionrate.common.api.ui.CategoryBuilder addSlider(String labelTranslationKey, long initialValue, long minimum, long maximum, Consumer<com.kwvanderlinde.fabricmc.villagerconversionrate.common.api.ui.SliderBuilder> callback) {
		LongSliderBuilder longSliderBuilder = entryBuilder.startLongSlider(new TranslatableText(labelTranslationKey), initialValue, minimum, maximum);
		callback.accept(new SliderBuilder(longSliderBuilder));
		category.addEntry(longSliderBuilder.build());
		return this;
	}
}
