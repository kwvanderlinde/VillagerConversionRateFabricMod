package com.kwvanderlinde.fabricmc.villagerconversionrate.config;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConfigurationSourceTest {
	private final ConfigurationLocator locator;
	private final ConfigurationParser parser;
	private final ConfigurationSource sut;

	ConfigurationSourceTest() {
		this.locator = mock(ConfigurationLocator.class);
		this.parser = mock(ConfigurationParser.class);

		this.sut = new ConfigurationSource(this.locator, this.parser);
	}

	@Test
	void testGetFirstTime_Successful() throws FileNotFoundException, ParseFailedException {
		Configuration expectedConfiguration = mock(Configuration.class);
		Reader reader = mock(Reader.class);
		when(this.locator.getReader()).thenReturn(reader);
		when(this.parser.parse(reader)).thenReturn(expectedConfiguration);

		Configuration result = sut.get();

		assertSame(expectedConfiguration, result, "The configuration should be the one returned by the parser.");

		verify(this.parser).parse(reader);
	}

	@Test
	void testGetFirstTime_FileNotFound() throws FileNotFoundException, ParseFailedException {
		Configuration expectedConfiguration = new Configuration();
		Reader reader = mock(Reader.class);
		when(this.locator.getReader()).thenThrow(new FileNotFoundException());

		Configuration result = sut.get();

		assertEquals(expectedConfiguration.enabled.get(), result.enabled.get());
		assertEquals(expectedConfiguration.conversionRate.get(), result.conversionRate.get());

		verify(this.parser, never()).parse(reader);
	}

	@Test
	void testGetFirstTime_ParseFailed() throws FileNotFoundException, ParseFailedException {
		Configuration expectedConfiguration = new Configuration();
		Reader reader = mock(Reader.class);
		when(this.locator.getReader()).thenReturn(reader);
		when(this.parser.parse(reader)).thenThrow(new ParseFailedException());

		Configuration result = sut.get();

		assertEquals(expectedConfiguration.enabled.get(), result.enabled.get());
		assertEquals(expectedConfiguration.conversionRate.get(), result.conversionRate.get());

		verify(this.parser).parse(reader);
	}

	@Test
	void testGetFirstTime_SuccessfulParse_but_AutoCloseException() throws FileNotFoundException, ParseFailedException, IOException {
		Configuration expectedConfiguration = mock(Configuration.class);
		Reader reader = mock(Reader.class);
		when(this.locator.getReader()).thenReturn(reader);
		when(this.parser.parse(reader)).thenReturn(expectedConfiguration);
		doThrow(new IOException()).when(reader).close();

		Configuration result = sut.get();

		assertSame(expectedConfiguration, result, "The configuration should be the one returned by the parser.");

		verify(this.parser).parse(reader);
	}

	@Test
	void testSave_Successful() throws FileNotFoundException, ParseFailedException {
		// region Setup
		// First load a configuration since we must have one before we can save it.
		// TODO Relax this requirements. No reason a save() can't precede a get(), even though it would be weird.
		Configuration expectedConfiguration = mock(Configuration.class);
		Reader reader = mock(Reader.class);
		when(this.locator.getReader()).thenReturn(reader);
		when(this.parser.parse(reader)).thenReturn(expectedConfiguration);
		// Must have a configuration loaded to save.
		sut.get();
		clearInvocations(this.locator, this.parser, expectedConfiguration);

		// Second, set up the writing expectations.
		Writer writer = mock(Writer.class);
		when(this.locator.getWriter()).thenReturn(writer);
		// endregion

		sut.save();

		verify(this.parser).unparse(writer, expectedConfiguration);
	}

	@Test
	void testSave_FileNotFound() throws FileNotFoundException, ParseFailedException {
		// region Setup
		// First load a configuration since we must have one before we can save it.
		// TODO Relax this requirements. No reason a save() can't precede a get(), even though it would be weird.
		Configuration expectedConfiguration = mock(Configuration.class);
		Reader reader = mock(Reader.class);
		when(this.locator.getReader()).thenReturn(reader);
		when(this.parser.parse(reader)).thenReturn(expectedConfiguration);
		// Must have a configuration loaded to save.
		sut.get();
		clearInvocations(this.locator, this.parser, expectedConfiguration);

		// Second, set up the writing expectations.
		Writer writer = mock(Writer.class);
		when(this.locator.getWriter()).thenThrow(new FileNotFoundException());
		// endregion

		sut.save();

		verify(this.parser, never()).unparse(any(), any());
	}

	@Test
	void testSave_Successful_but_AutoCloseException() throws FileNotFoundException, ParseFailedException, IOException {
		// region Setup
		// First load a configuration since we must have one before we can save it.
		// TODO Relax this requirements. No reason a save() can't precede a get(), even though it would be weird.
		Configuration expectedConfiguration = mock(Configuration.class);
		Reader reader = mock(Reader.class);
		when(this.locator.getReader()).thenReturn(reader);
		when(this.parser.parse(reader)).thenReturn(expectedConfiguration);
		// Must have a configuration loaded to save.
		sut.get();
		clearInvocations(this.locator, this.parser, expectedConfiguration);

		// Second, set up the writing expectations.
		Writer writer = mock(Writer.class);
		doThrow(new IOException()).when(writer).close();
		when(this.locator.getWriter()).thenReturn(writer);
		// endregion

		sut.save();

		verify(this.parser).unparse(writer, expectedConfiguration);
	}
}
