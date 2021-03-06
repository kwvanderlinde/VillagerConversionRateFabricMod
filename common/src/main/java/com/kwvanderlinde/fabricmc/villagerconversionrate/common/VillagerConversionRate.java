package com.kwvanderlinde.fabricmc.villagerconversionrate.common;

import com.kwvanderlinde.fabricmc.villagerconversionrate.config.ConfigurationLocator;
import com.kwvanderlinde.fabricmc.villagerconversionrate.config.ConfigurationParser;
import com.kwvanderlinde.fabricmc.villagerconversionrate.config.ConfigurationSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Random;

public class VillagerConversionRate {
	public static final String MOD_NAME = "villager_conversion_rate";

	private static final Logger LOGGER = LogManager.getFormatterLogger(VillagerConversionRate.class.getCanonicalName());

	private static VillagerConversionRate instance = null;

	public static VillagerConversionRate getInstance() {
		return Objects.requireNonNull(instance);
	}

	public static VillagerConversionRate initialize(Path configurationDirectory) {
		if (instance != null) {
			throw new RuntimeException("VillagerConversionRate has already been initialized.");
		}

		ConfigurationSource configurationSource = new ConfigurationSource(
				new ConfigurationLocator(configurationDirectory, MOD_NAME),
				new ConfigurationParser()
		);
		instance = new VillagerConversionRate(configurationSource,
		                                      new VillagerConversionPredicate(new Random(), configurationSource),
		                                      new MenuFactory(configurationSource));

		return instance;
	}

	private final ConfigurationSource configurationSource;
	private final VillagerConversionPredicate villagerConversionPredicate;
	private final MenuFactory menuFactory;

	private VillagerConversionRate(ConfigurationSource configurationSource,
	                               VillagerConversionPredicate villagerConversionPredicate,
	                               MenuFactory menuFactory) {
		this.configurationSource = configurationSource;
		this.villagerConversionPredicate = villagerConversionPredicate;
		this.menuFactory = menuFactory;

		LOGGER.info(String.format("Initializing %s mod", MOD_NAME));

		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		// Ensure that the configuration is loaded.
		this.configurationSource.get();

		LOGGER.info(String.format("Finished initializing %s mod", MOD_NAME));

		instance = this;

	}

	public ConfigurationSource getConfigurationSource() {
		return this.configurationSource;
	}

	public VillagerConversionPredicate getConversionPredicate() {
		return this.villagerConversionPredicate;
	}

	public MenuFactory getMenuFactory() {
		return this.menuFactory;
	}
}
