package net.xstream.bungee.utils;

import net.xstream.bungee.XStream;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.logging.Logger;

public final class LogPrinter {
	private static final Logger LOGGER = XStream.instance().getLogger();
	
	public static void info(@NotNull String... strings) {
		Arrays.asList(strings).forEach(LOGGER::info);
	}
	
	public static void warn(@NotNull String... strings) {
		Arrays.asList(strings).forEach(LOGGER::warning);
	}
	
	public static void error(@NotNull String... strings) {
		Arrays.asList(strings).forEach(LOGGER::severe);
	}
}
