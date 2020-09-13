package com.kwvanderlinde.fabricmc.villagerconversionrate.common.api.ui;

import java.util.function.Consumer;

public interface CategoryBuilder {
	CategoryBuilder addToggle(String labelTranslationKey, boolean initialValue, Consumer<ToggleBuilder> callback);

	CategoryBuilder addSlider(String labelTranslationKey, long initialValue, long minimum, long maximum, Consumer<SliderBuilder> callback);
}
