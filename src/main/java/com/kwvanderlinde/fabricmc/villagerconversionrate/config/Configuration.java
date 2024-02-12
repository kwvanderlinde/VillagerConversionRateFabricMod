package com.kwvanderlinde.fabricmc.villagerconversionrate.config;

public record Configuration(boolean enabled, double conversionRate) {
    public Configuration() {
        this(false, 0.5);
    }
}
