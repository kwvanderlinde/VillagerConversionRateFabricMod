package com.kwvanderlinde.mc.villagerconversionrate;

import com.google.common.io.Files;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class ConfigurationLocator {
	private static final Logger LOGGER = LogManager.getFormatterLogger(ConfigurationLocator.class.getCanonicalName());

	private final FabricLoader loader;
	private final String modName;

	public ConfigurationLocator(FabricLoader loader, String modName) {
		this.loader = loader;
		this.modName = modName;
	}

	private Path getConfigFilePath() {
		return this.loader.getConfigDirectory().toPath().resolve(this.modName + ".json");
	}

	public Reader getReader() throws FileNotFoundException {
		return Files.newReader(getConfigFilePath().toFile(), StandardCharsets.UTF_8);
	}

	public Writer getWriter() throws FileNotFoundException {
		return Files.newWriter(getConfigFilePath().toFile(), StandardCharsets.UTF_8);
	}
}
