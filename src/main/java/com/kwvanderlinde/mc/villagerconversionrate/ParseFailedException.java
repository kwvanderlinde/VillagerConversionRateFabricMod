package com.kwvanderlinde.mc.villagerconversionrate;

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
