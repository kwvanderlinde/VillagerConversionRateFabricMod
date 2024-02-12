package com.kwvanderlinde.fabricmc.villagerconversionrate.config;

public record Configuration(boolean enabled, double conversionRate) {
    public Configuration() {
        this(false, 0.5);
    }

    public Configuration validated() {
        return new Configuration(
                enabled,
                Math.max(0.0, Math.min(conversionRate, 1.0))
        );
    }

    public Configuration withEnabled(boolean enabled) {
        return new Configuration(enabled, this.conversionRate).validated();
    }

    public Configuration withConversionRate(double rate) {
        return new Configuration(this.enabled, rate).validated();
    }
}
