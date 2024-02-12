package com.kwvanderlinde.fabricmc.villagerconversionrate.config;

import com.google.common.io.Files;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class ConfigurationLocator {
	private final Path configDirectory;
	private final String modName;

	public ConfigurationLocator(Path configDirectory, String modName) {
		this.configDirectory = configDirectory;
		this.modName = modName;
	}

	private Path getConfigFilePath() {
		return this.configDirectory.resolve(this.modName + ".json");
	}

	public Reader getReader() throws FileNotFoundException {
		return Files.newReader(getConfigFilePath().toFile(), StandardCharsets.UTF_8);
	}

	public Writer getWriter() throws FileNotFoundException {
		return Files.newWriter(getConfigFilePath().toFile(), StandardCharsets.UTF_8);
	}
}
