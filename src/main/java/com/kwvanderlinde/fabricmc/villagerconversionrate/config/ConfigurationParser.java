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
			return gson.fromJson(reader, Configuration.class);
		}
		catch (JsonParseException e) {
			throw new ParseFailedException(e);
		}
	}

	public void unparse(Writer writer, Configuration configuration) {
		this.gson.toJson(configuration, writer);
	}
}
