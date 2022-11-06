package net.xstream.plugin.utils;

import net.xstream.plugin.XStream;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Utility class to send multiple logs easy and rapidly.
 *
 * @author InitSync
 * @version 1.0.1
 * @since 1.0.0
 */
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
