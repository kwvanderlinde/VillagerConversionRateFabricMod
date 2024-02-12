package com.kwvanderlinde.fabricmc.villagerconversionrate.common;

import com.kwvanderlinde.fabricmc.villagerconversionrate.config.Configuration;
import com.kwvanderlinde.fabricmc.villagerconversionrate.config.ConfigurationSource;

import java.util.Random;

public class VillagerConversionPredicate {
	public enum ConversionResult {
		USE_VANILLA_BEHAVIOUR,
		CONVERT,
		KILL,
	}

	private final Random random;
	private final ConfigurationSource configurationSource;

	public VillagerConversionPredicate(Random random, ConfigurationSource configurationSource) {
		this.random = random;
		this.configurationSource = configurationSource;
	}

	/**
	 * Checks whether a villager should be converted.
	 *
	 * @return A flag indicating what action for the caller to take:
	 * - USE_VANILLA_BEHAVIOUR: Fallback to vanilla behaviour, as though the mod does not exist.
	 * - CONVERT: Convert the villager to a zombie.
	 * - KILL: Kill the villager without converting it to a zombie.
	 */
	public ConversionResult test() {
		Configuration configuration = configurationSource.get();
		if (!configuration.enabled()) {
			// Customized conversion rate disabled. Default to vanilla behaviour.
			return ConversionResult.USE_VANILLA_BEHAVIOUR;
		}

		double conversionRate = configuration.conversionRate();
		if ((conversionRate == 0.0)                              // 0.0 means it is impossible for conversion to occur.
				|| this.random.nextDouble() > conversionRate) {  // N.B.: `this.random.nextDouble() <= 1.0` is always `true`.
			// Possible conversion failed. Do nothing and let the villager die.
			return ConversionResult.KILL;
		}

		return ConversionResult.CONVERT;
	}
}
