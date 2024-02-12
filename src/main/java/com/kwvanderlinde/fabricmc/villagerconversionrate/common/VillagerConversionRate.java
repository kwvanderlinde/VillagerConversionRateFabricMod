package com.kwvanderlinde.fabricmc.villagerconversionrate.common;

import com.kwvanderlinde.fabricmc.villagerconversionrate.config.ConfigurationLocator;
import com.kwvanderlinde.fabricmc.villagerconversionrate.config.ConfigurationParser;
import com.kwvanderlinde.fabricmc.villagerconversionrate.config.ConfigurationSource;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
		                                      new VillagerConversionPredicate(new Random(), configurationSource));

		return instance;
	}

	private final ConfigurationSource configurationSource;
	private final VillagerConversionPredicate villagerConversionPredicate;

	private VillagerConversionRate(ConfigurationSource configurationSource,
	                               VillagerConversionPredicate villagerConversionPredicate) {
		this.configurationSource = configurationSource;
		this.villagerConversionPredicate = villagerConversionPredicate;

		LOGGER.info(String.format("Initializing %s mod", MOD_NAME));

		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		// Ensure that the configuration is loaded.
		this.configurationSource.load();

		LOGGER.info(String.format("Finished initializing %s mod", MOD_NAME));

		instance = this;


		CommandRegistrationCallback.EVENT.register(this::registerCommands);
	}

	public VillagerConversionPredicate getConversionPredicate() {
		return this.villagerConversionPredicate;
	}

	private void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment) {
		final var commands = Commands.literal("vcr");

		commands
				.then(Commands.literal("reload")
				              .requires(s -> s.hasPermission(4))
				              .executes(context -> {
								  reload();
								  return 0;
							  }));

		dispatcher.register(commands);
	}

	private void reload() {
		this.configurationSource.load();
	}
}
