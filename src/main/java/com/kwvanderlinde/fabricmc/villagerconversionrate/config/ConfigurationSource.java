package com.kwvanderlinde.fabricmc.villagerconversionrate.config;

import com.google.common.io.Files;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.function.Function;

public class ConfigurationSource {
	private static final Logger LOGGER = LogManager.getFormatterLogger(ConfigurationSource.class.getCanonicalName());

	private final Path configPath;
	private final Gson gson;
	private Configuration configuration;

	public ConfigurationSource(Path configPath) {
		this.configPath = configPath;
		this.gson = new GsonBuilder()
				.setPrettyPrinting()
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
				.create();
		this.configuration = null;
	}

	public void load() {
		var result = new Configuration();

		try (Reader reader = Files.newReader(configPath.toFile(), StandardCharsets.UTF_8)) {
			result = gson.fromJson(reader, Configuration.class).validated();
			LOGGER.info("Configuration loaded");
		}
		catch (JsonParseException e) {
			LOGGER.info("Configuration file was corrupt so we are recreating it with default values.");
			result = new Configuration();
			this.save();
		}
		catch (FileNotFoundException e) {
			LOGGER.info("Configuration file was not found so we are creating it now with default values.");
			result = new Configuration();
			this.save();
		}
		catch (IOException e) {
			LOGGER.error("Unexpected I/O error while reading configuration file. Falling back to defaults.", e);
			// configuration is guaranteed to be set here. TODO Make this obvious in the code flow.
		}

		this.configuration = result;
	}

	public Configuration get() {
		if (this.configuration == null) {
			// Not yet loaded.
			load();
		}
		assert this.configuration != null;

		return this.configuration;
	}

	public void updateConfiguration(Function<Configuration, Configuration> updater) {
		this.configuration = updater.apply(this.configuration);
		this.save();
	}

	private void save() {
		try (Writer writer = Files.newWriter(configPath.toFile(), StandardCharsets.UTF_8)) {
			this.gson.toJson(configuration, writer);
		}
		catch (FileNotFoundException e) {
			LOGGER.error("Unable to find the configuration file to write to.");
		}
		catch (IOException e) {
			LOGGER.error("Unexpected I/O error while writing configuration file", e);
		}
	}
}
