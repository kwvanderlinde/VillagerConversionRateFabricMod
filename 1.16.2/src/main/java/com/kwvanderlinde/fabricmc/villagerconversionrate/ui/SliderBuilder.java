package com.kwvanderlinde.fabricmc.villagerconversionrate.ui;

import me.shedaniel.clothconfig2.impl.builders.LongSliderBuilder;
import net.minecraft.text.TranslatableText;

import java.util.function.Consumer;

public class SliderBuilder implements com.kwvanderlinde.fabricmc.villagerconversionrate.common.api.ui.SliderBuilder {
	private final LongSliderBuilder longSliderBuilder;

	public SliderBuilder(LongSliderBuilder longSliderBuilder) {
		this.longSliderBuilder = longSliderBuilder;
	}

	@Override
	public com.kwvanderlinde.fabricmc.villagerconversionrate.common.api.ui.SliderBuilder setDefaultValue(Long defaultValue) {
		this.longSliderBuilder.setDefaultValue(defaultValue);
		return this;
	}

	@Override
	public com.kwvanderlinde.fabricmc.villagerconversionrate.common.api.ui.SliderBuilder setTooltip(String tooltipTranslationKey) {
		this.longSliderBuilder.setTooltip(new TranslatableText(tooltipTranslationKey));
		return this;
	}

	@Override
	public com.kwvanderlinde.fabricmc.villagerconversionrate.common.api.ui.SliderBuilder onSave(Consumer<Long> saveHandler) {
		this.longSliderBuilder.setSaveConsumer(saveHandler);
		return this;
	}
}
