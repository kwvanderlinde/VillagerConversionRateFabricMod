package com.kwvanderlinde.fabricmc.villagerconversionrate.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConfigurationParserTest {
	private final ConfigurationParser sut;

	public ConfigurationParserTest() {
		this.sut = new ConfigurationParser();
	}

	@ParameterizedTest
	@MethodSource("parseProviderSuccess")
	void testParseSuccess(String json, boolean expectedEnabled, double expectedConversionRate) throws ParseFailedException {
		Configuration result = this.sut.parse(new StringReader(json));

		// These are dynamic values.
		assertEquals(expectedEnabled, result.enabled.get());
		assertEquals(expectedConversionRate, result.conversionRate.get());
		// These should always be the same no matter what the configuration is.
		assertEquals(false, result.enabled.getDefaultValue());
		assertEquals(0.5, result.conversionRate.getDefaultValue());
		assertEquals(0.0, result.conversionRate.getMinimum());
		assertEquals(1.0, result.conversionRate.getMaximum());
	}

	static Stream<Arguments> parseProviderSuccess() {
		return Stream.of(
				Arguments.of("{\"enabled\": true, \"conversion-rate\": 0.8}", true, 0.8),
				Arguments.of("{\"conversion-rate\": 0.8}", false, 0.8),
				Arguments.of("{\"enabled\": true}", true, 0.5),
				Arguments.of("{}", false, 0.5)
		);
	}

	@Test
	void testParseFailure() {
		String json = "{";
		assertThrows(ParseFailedException.class, () -> this.sut.parse(new StringReader(json)));
	}

	@Test
	void testUnparse() {
		Configuration configuration = new Configuration();
		configuration.enabled.set(false);
		configuration.conversionRate.set(0.4);
		StringWriter writer = new StringWriter();

		this.sut.unparse(writer, configuration);

		// TODO Reparse the string to verify the contents rather than requiring a specific character layout.
		String contents = writer.toString();
		assertEquals("{\n  \"enabled\": false,\n  \"conversion-rate\": 0.4\n}", contents);
	}
}
