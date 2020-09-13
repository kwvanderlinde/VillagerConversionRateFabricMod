package com.kwvanderlinde.fabricmc.villagerconversionrate;

import com.kwvanderlinde.fabricmc.villagerconversionrate.config.ConfigurationLocator;
import com.kwvanderlinde.fabricmc.villagerconversionrate.config.ConfigurationParser;
import com.kwvanderlinde.fabricmc.villagerconversionrate.config.ConfigurationSource;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

// TODO Server mod initializer.
public class VillagerConversionRateModInitializer implements ModInitializer {
	public static final String MOD_NAME = "villager_conversion_rate";

	private static final Logger LOGGER = LogManager.getFormatterLogger(VillagerConversionRateModInitializer.class.getCanonicalName());

	private static VillagerConversionRateModInitializer instance = null;

	public static VillagerConversionRateModInitializer getInstance() {
		return Objects.requireNonNull(instance);
	}

	private final ConfigurationSource configurationSource;

	public VillagerConversionRateModInitializer() {
		this.configurationSource = new ConfigurationSource(
				new ConfigurationLocator(FabricLoader.getInstance().getConfigDir(), MOD_NAME),
				new ConfigurationParser()
		);
	}

	@Override
	public void onInitialize() {
		LOGGER.info(String.format("Initializing %s mod", MOD_NAME));

		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		// Ensure that the configuration is loaded.
		this.configurationSource.get();

		instance = this;

		LOGGER.info(String.format("Finished initializing %s mod", MOD_NAME));
	}

	public ConfigurationSource getConfigurationSource() {
		return this.configurationSource;
	}
}
