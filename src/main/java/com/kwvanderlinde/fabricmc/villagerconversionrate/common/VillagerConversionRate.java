package com.kwvanderlinde.fabricmc.villagerconversionrate.common;

import com.kwvanderlinde.fabricmc.villagerconversionrate.config.ConfigurationSource;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
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
				configurationDirectory.resolve(MOD_NAME + ".json")
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

		CommandRegistrationCallback.EVENT.register(this::registerCommands);
	}

	public VillagerConversionPredicate getConversionPredicate() {
		return this.villagerConversionPredicate;
	}

	private void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment) {
		final var commands = Commands.literal("vcr");

		commands
				.executes(context -> {
					final var enabled = configurationSource.get().enabled();
					final var rate = configurationSource.get().conversionRate();
					context.getSource().sendSuccess(
							() -> Component.literal("Villager conversion rate [enabled: ").append(Boolean.toString(enabled)).append("; rate: ").append(Double.toString(rate)).append("]"),
							false
					);
					return 0;
				})
				.then(Commands.literal("reload")
				              .requires(s -> s.hasPermission(4))
				              .executes(context -> {
								  this.configurationSource.load();

								  // TODO Handle failure here.
					              context.getSource().sendSuccess(
							              () -> Component.literal("Configuration reloaded"),
							              false
					              );

								  return 0;
							  }))
				.then(Commands.literal("rate")
						      .executes(context -> {
								  final var rate = configurationSource.get().conversionRate();
								  context.getSource().sendSuccess(
										  () -> Component.literal("Villager conversion rate is set to ").append(Double.toString(rate)),
										  false
								  );
								  return 0;
						      })
						      .then(Commands.argument("rate", DoubleArgumentType.doubleArg(0.0, 1.0))
						                    .requires(s -> s.hasPermission(4))
								            .executes(context -> {
												final var rate = DoubleArgumentType.getDouble(context, "rate");
									            configurationSource.updateConfiguration(configuration -> configuration.withConversionRate(rate));
									            context.getSource().sendSuccess(
											            () -> Component.literal("Villager conversion rate now set to ").append(Double.toString(rate)),
											            false
									            );
												return 0;
								            })))
				.then(Commands.literal("enabled")
						      .executes(context -> {
							      final var enabled = configurationSource.get().enabled();
							      context.getSource().sendSuccess(
									      () -> Component.literal("Villager conversion rate is").append(enabled ? " " : " not ").append("enabled"),
									      false
							      );
							      return 0;
						      })
				              .then(Commands.argument("enabled", BoolArgumentType.bool())
				                            .requires(s -> s.hasPermission(4))
				                            .executes(context -> {
												final var enabled = BoolArgumentType.getBool(context, "enabled");
					                            configurationSource.updateConfiguration(configuration -> configuration.withEnabled(enabled));
					                            context.getSource().sendSuccess(
							                            () -> Component.literal("Villager conversion rate is now ").append(enabled ? "enabled" : "disabled"),
							                            false
					                            );
					                            return 0;
				                            })));

		dispatcher.register(commands);
	}
}
