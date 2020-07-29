package com.kwvanderlinde.mc.villagerconversionrate;

import com.google.common.io.CharStreams;
import net.fabricmc.loader.api.FabricLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import static org.mockito.Mockito.*;

public class ConfigurationLocatorTest {
	private final FabricLoader loader;
	private final String modName;
	private final ConfigurationLocator sut;
	private final Path temporaryDirectory;

	public ConfigurationLocatorTest() {
		this.loader = mock(FabricLoader.class);
		this.modName = "testMod";
		try {
			this.temporaryDirectory = Files.createTempDirectory(null);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		when(this.loader.getConfigDirectory()).thenReturn(this.temporaryDirectory.toFile());

		this.sut = new ConfigurationLocator(this.loader, this.modName);
	}

	@AfterEach
	void tearDown() {
		try {
			Files.walk(this.temporaryDirectory)
			     .sorted(Comparator.reverseOrder())
			     .map(Path::toFile)
			     .forEach(File::delete);
		}
		catch (IOException e) {
			// Meh. It's just cleanup.
			e.printStackTrace();
		}
	}

	@Test
	void testReaderForExistingFile() throws IOException {
		Files.write(this.temporaryDirectory.resolve(this.modName + ".json"), "some file contents".getBytes(StandardCharsets.UTF_8));

		try(Reader reader = this.sut.getReader()) {
			// Verify that the correct file was accessed.
			String contents = CharStreams.toString(reader);

			assertEquals("some file contents", contents);
		}
	}

	@Test
	void testReaderNonExistentFiles() {
		// We intentionally do not write the config file.
		assertThrows(FileNotFoundException.class, this.sut::getReader);
	}

	@Test
	void testWriterForExistingFile() throws IOException {
		Files.write(this.temporaryDirectory.resolve(this.modName + ".json"), "some file contents".getBytes(StandardCharsets.UTF_8));

		try(Writer writer = this.sut.getWriter()) {
			writer.write("some new contents");
		}

		// Verify that the correct file was accessed.
		String contents = new String(Files.readAllBytes(this.temporaryDirectory.resolve(this.modName + ".json")), StandardCharsets.UTF_8);
		assertEquals("some new contents", contents);
	}

	@Test
	void testWriterForNonExistentFile() throws IOException {
		// We intentionally do not write the config file during setup.

		try(Writer writer = this.sut.getWriter()) {
			writer.write("some new contents");
		}
		catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		catch (IOException e) {
			// This is just from the .close() method, so it's not a big deal.
			e.printStackTrace();
		}

		// Verify that the correct file was accessed.
		String contents = new String(Files.readAllBytes(this.temporaryDirectory.resolve(this.modName + ".json")), StandardCharsets.UTF_8);
		assertEquals("some new contents", contents);
	}
}
