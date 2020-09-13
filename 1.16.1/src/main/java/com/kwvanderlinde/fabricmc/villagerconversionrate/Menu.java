package com.kwvanderlinde.fabricmc.villagerconversionrate;

import com.kwvanderlinde.fabricmc.villagerconversionrate.common.VillagerConversionRate;
import com.kwvanderlinde.fabricmc.villagerconversionrate.config.Configuration;
import com.kwvanderlinde.fabricmc.villagerconversionrate.config.ConfigurationSource;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.TranslatableText;

public class Menu implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {

		return screen -> {
			VillagerConversionRate initializer = VillagerConversionRate.getInstance();
			ConfigurationSource configurationSource = initializer.getConfigurationSource();
			Configuration configuration = configurationSource.get();

			ConfigBuilder builder = ConfigBuilder.create()
			                                     .setParentScreen(screen)
			                                     .setTitle(new TranslatableText("title.villager_conversion_rate.config"))
			                                     .setSavingRunnable(initializer.getConfigurationSource()::save);

			ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("category.villager_conversion_rate.general"));

			ConfigEntryBuilder entryBuilder = builder.entryBuilder();
			general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("option.villager_conversion_rate.enabled"), configuration.enabled.get())
			                             .setDefaultValue(configuration.enabled.getDefaultValue())
			                             .setSaveConsumer(configuration.enabled::set)
			                             .build());
			general.addEntry(entryBuilder.startLongSlider(new TranslatableText("option.villager_conversion_rate.conversionRate"),
			                                              conversionRateToSliderValue(configuration.conversionRate.get()),
			                                              conversionRateToSliderValue(configuration.conversionRate.getMinimum()),
			                                              conversionRateToSliderValue(configuration.conversionRate.getMaximum())
			                                              )
			                             .setDefaultValue(conversionRateToSliderValue(configuration.conversionRate.getDefaultValue()))
			                             .setTooltip(new TranslatableText("option.villager_conversion_rate.conversionRateTooltip"))
			                             .setSaveConsumer(value -> configuration.conversionRate.set(sliderValueToConversionRate(value)))
			                             .build()
			);

			return builder.build();
		};
	}

	private long conversionRateToSliderValue(double value) {
		return Math.round(value * 100.0);
	}

	private double sliderValueToConversionRate(long value) {
		return value / 100.0;
	}
}
