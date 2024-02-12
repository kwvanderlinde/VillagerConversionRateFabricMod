package com.kwvanderlinde.fabricmc.villagerconversionrate.config;

import com.google.common.primitives.Doubles;

// TODO Separate a real configuration from a configuration model? The model has bounds, etc, while the configuration just is what it is. Also immutable, with downstream required to get it each time it is needed?
public class Configuration {
	/**
	 * If `false`, use vanilla behaviour. Otherwise use `conversionRate` to determine the conversion rate regardless of
	 * difficulty.
	 */
	public final Property<Boolean> enabled = new Property<>(false);

	/**
	 * The villager-to-zombie conversion rate.
	 */
	public final DoubleProperty conversionRate = new DoubleProperty(0.5, 0.0, 1.0);

	public Configuration() {
	}

	public static class Property<T> {
		private final T defaultValue;
		private T value;

		public Property(T defaultValue) {
			this.defaultValue = defaultValue;
			this.value = defaultValue;
		}

		public T getDefaultValue() {
			return this.defaultValue;
		}

		public T get() {
			return this.value;
		}

		public void set(T value) {
			this.value = value;
		}
	}

	public static class DoubleProperty extends Property<Double> {
		private final double minimum;
		private final double maximum;

		public DoubleProperty(double defaultValue, double minimum, double maximum) {
			super(defaultValue);

			this.minimum = minimum;
			this.maximum = maximum;
		}

		@Override
		public void set(Double value) {
			super.set(Doubles.constrainToRange(value, this.minimum, this.maximum));
		}

		public double getMinimum() {
			return minimum;
		}

		public double getMaximum() {
			return maximum;
		}
	}
}
