package com.kwvanderlinde.fabricmc.villagerconversionrate.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

public class ConfigurationParser {
	private final Gson gson;

	public ConfigurationParser() {
		this.gson = new GsonBuilder()
				.setPrettyPrinting()
				.registerTypeAdapter(Configuration.class, new ConfigurationSerializer())
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

	private static class ConfigurationSerializer implements JsonDeserializer<Configuration>, JsonSerializer<Configuration> {
		@Override
		public Configuration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jo = json.getAsJsonObject();

			Configuration configuration = new Configuration();
			JsonElement enabledElement = jo.get("enabled");
			if (enabledElement != null && enabledElement.isJsonPrimitive() && ((JsonPrimitive)enabledElement).isBoolean()) {
				configuration.enabled.set(enabledElement.getAsBoolean());
			}
			JsonElement conversionRateElement = jo.get("conversion-rate");
			if (conversionRateElement != null && conversionRateElement.isJsonPrimitive() && ((JsonPrimitive)conversionRateElement).isNumber()) {
				configuration.conversionRate.set(conversionRateElement.getAsDouble());
			}

			return configuration;
		}

		@Override
		public JsonElement serialize(Configuration src, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject result = new JsonObject();
			result.add("enabled", new JsonPrimitive(src.enabled.get()));
			result.add("conversion-rate", new JsonPrimitive(src.conversionRate.get()));
			return result;
		}
	}
}
