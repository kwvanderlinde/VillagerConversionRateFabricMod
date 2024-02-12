package com.kwvanderlinde.fabricmc.villagerconversionrate;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class VillagerConversionRateModInitializer implements ModInitializer {
	@Override
	public void onInitialize() {
		VillagerConversionRate.initialize(FabricLoader.getInstance().getConfigDir());
	}
}
