package com.kwvanderlinde.fabricmc.villagerconversionrate.common.api.ui;

import java.util.function.Consumer;

public interface ValueSelectingControlBuilder<Self, T> {
	Self setDefaultValue(T defaultValue);

	Self setTooltip(String tooltipTranslationKey);

	Self onSave(Consumer<T> saveHandler);
}
