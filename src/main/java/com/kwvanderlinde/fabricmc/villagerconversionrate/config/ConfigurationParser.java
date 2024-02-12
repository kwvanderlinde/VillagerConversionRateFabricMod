package com.kwvanderlinde.fabricmc.villagerconversionrate.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.io.Reader;
import java.io.Writer;

public class ConfigurationParser {
	private final Gson gson;

	public ConfigurationParser() {
		this.gson = new GsonBuilder()
				.setPrettyPrinting()
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
				.create();
	}

	public Configuration parse(Reader reader) throws ParseFailedException {
		try {
			Configuration parsed = gson.fromJson(reader, Configuration.class);
			// Validate.
			return new Configuration(
					parsed.enabled(),
					Math.max(0.0, Math.min(parsed.conversionRate(), 1.0))
			);
		}
		catch (JsonParseException e) {
			throw new ParseFailedException(e);
		}
	}

	public void unparse(Writer writer, Configuration configuration) {
		this.gson.toJson(configuration, writer);
	}
}
