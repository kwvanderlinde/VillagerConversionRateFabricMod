package com.kwvanderlinde.fabricmc.villagerconversionrate.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class ConfigurationSource {
	private static final Logger LOGGER = LogManager.getFormatterLogger(ConfigurationSource.class.getCanonicalName());

	private final ConfigurationLocator locator;
	private final ConfigurationParser parser;
	private Configuration configuration;

	public ConfigurationSource(ConfigurationLocator locator, ConfigurationParser parser) {
		this.locator = locator;
		this.parser = parser;
		this.configuration = null;
	}

	public void load() {
		var result = new Configuration();

		try (Reader reader = locator.getReader()) {
			result = this.parser.parse(reader);
			LOGGER.info("Configuration loaded");
		}
		catch (ParseFailedException e) {
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

	public void save() {
		try (Writer writer = locator.getWriter()) {
			this.parser.unparse(writer, this.configuration);
		}
		catch (FileNotFoundException e) {
			LOGGER.error("Unable to find the configuration file to write to.");
		}
		catch (IOException e) {
			LOGGER.error("Unexpected I/O error while writing configuration file", e);
		}
	}
}
