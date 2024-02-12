package com.kwvanderlinde.fabricmc.villagerconversionrate.common;

import com.kwvanderlinde.fabricmc.villagerconversionrate.common.api.ui.MenuBuilder;
import com.kwvanderlinde.fabricmc.villagerconversionrate.config.Configuration;
import com.kwvanderlinde.fabricmc.villagerconversionrate.config.ConfigurationSource;

public class MenuFactory {
	private final ConfigurationSource configurationSource;

	public MenuFactory(ConfigurationSource configurationSource) {
		this.configurationSource = configurationSource;
	}

	public void build(MenuBuilder builder) {
		Configuration configuration = this.configurationSource.get();

		builder.setTitle("title.villager_conversion_rate.config")
		       .onSave(this.configurationSource::save)
		       .newCategory("category.villager_conversion_rate.general", categoryBuilder -> {
			        categoryBuilder
					        .addToggle("option.villager_conversion_rate.enabled", configuration.enabled.get(), toggleBuilder -> {
					        	toggleBuilder.setDefaultValue(configuration.enabled.getDefaultValue())
						                     .onSave(configuration.enabled::set);
					        })
					        .addSlider("option.villager_conversion_rate.conversionRate", conversionRateToSliderValue(configuration.conversionRate.get()), conversionRateToSliderValue(configuration.conversionRate.getMinimum()), conversionRateToSliderValue(configuration.conversionRate.getMaximum()), sliderBuilder -> {
					        	sliderBuilder.setDefaultValue(conversionRateToSliderValue(configuration.conversionRate.getDefaultValue()))
						                     .setTooltip("option.villager_conversion_rate.conversionRateTooltip")
						                     .onSave(value -> configuration.conversionRate.set(sliderValueToConversionRate(value)));
					        });
		       });
	}


	private long conversionRateToSliderValue(double value) {
		return Math.round(value * 100.0);
	}

	private double sliderValueToConversionRate(long value) {
		return value / 100.0;
	}
}
