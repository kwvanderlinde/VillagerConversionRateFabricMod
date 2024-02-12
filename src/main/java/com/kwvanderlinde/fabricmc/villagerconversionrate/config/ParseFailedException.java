package com.kwvanderlinde.fabricmc.villagerconversionrate.config;

public class ParseFailedException extends Exception {
	ParseFailedException() {
		super();
	}

	ParseFailedException(String message) {
		super(message);
	}

	ParseFailedException(Throwable cause) {
		super(cause);
	}

	ParseFailedException(String message, Throwable cause) {
		super(message, cause);
	}
}
