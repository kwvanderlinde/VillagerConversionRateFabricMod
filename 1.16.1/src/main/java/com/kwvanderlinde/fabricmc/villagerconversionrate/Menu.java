package com.kwvanderlinde.fabricmc.villagerconversionrate;

import com.kwvanderlinde.fabricmc.villagerconversionrate.common.VillagerConversionRate;
import com.kwvanderlinde.fabricmc.villagerconversionrate.ui.MenuBuilder;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;

public class Menu implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return screen -> {
			ConfigBuilder builder = ConfigBuilder.create()
			                                     .setParentScreen(screen);
			MenuBuilder menuBuilder = new MenuBuilder(builder);
			builder.setSavingRunnable(() -> menuBuilder.getOnSaveCallbacks().forEach(Runnable::run));

			VillagerConversionRate.getInstance().getMenuFactory().build(menuBuilder);

			return builder.build();
		};
	}
}
