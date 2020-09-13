package com.kwvanderlinde.fabricmc.villagerconversionrate.ui;

import me.shedaniel.clothconfig2.impl.builders.BooleanToggleBuilder;
import net.minecraft.text.TranslatableText;

import java.util.function.Consumer;

public class ToggleBuilder implements com.kwvanderlinde.fabricmc.villagerconversionrate.common.api.ui.ToggleBuilder {
	private final BooleanToggleBuilder booleanToggleBuilder;

	public ToggleBuilder(BooleanToggleBuilder booleanToggleBuilder) {
		this.booleanToggleBuilder = booleanToggleBuilder;
	}

	@Override
	public com.kwvanderlinde.fabricmc.villagerconversionrate.common.api.ui.ToggleBuilder setDefaultValue(Boolean defaultValue) {
		this.booleanToggleBuilder.setDefaultValue(defaultValue);
		return this;
	}

	@Override
	public com.kwvanderlinde.fabricmc.villagerconversionrate.common.api.ui.ToggleBuilder setTooltip(String tooltipTranslationKey) {
		this.booleanToggleBuilder.setTooltip(new TranslatableText(tooltipTranslationKey));
		return this;
	}

	@Override
	public com.kwvanderlinde.fabricmc.villagerconversionrate.common.api.ui.ToggleBuilder onSave(Consumer<Boolean> saveHandler) {
		this.booleanToggleBuilder.setSaveConsumer(saveHandler);
		return this;
	}
}
