package net.xstream.api.config;

import net.xstream.api.enums.Action;
import net.xstream.api.enums.File;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Model for the ConfigurationHandler.
 *
 * @author InitSync
 * @version 1.0.0
 * @since 1.0.0
 */
public interface HandlerConfig {
	/**
	 * Do something with the file specified.
	 *
	 * @param file File type.
	 * @param action Action type.
	 * @param toPath Path for the object.
	 * @param object Object to set.
	 */
	void doSomething(
		 @NotNull File file,
		 @NotNull Action action,
		 @Nullable String toPath,
		 @NotNull Object object
	);
	
	/**
	 * Returns a String from the file.
	 *
	 * @param file File type.
	 * @param path Path to get.
	 * @return A string.
	 */
	@NotNull String text(@NotNull File file, @NotNull String path);
	
	/**
	 * Returns a string list.
	 *
	 * @param file File type.
	 * @param path Path to get.
	 * @return A string list.
	 */
	@NotNull List<String> textList(@NotNull File file, @NotNull String path);
	
	/**
	 * Returns an ConfigurationSection object.
	 *
	 * @param file File type.
	 * @param path Path to get.
	 * @return A ConfigurationSection object.
	 */
	@Nullable ConfigurationSection section(@NotNull File file, @NotNull String path);
	
	/**
	 * Returns a integer value.
	 *
	 * @param file File type.
	 * @param path Path to get.
	 * @return A int.
	 */
	int number(@NotNull File file, @NotNull String path);
	
	/**
	 * Returns a boolean value.
	 *
	 * @param file File type.
	 * @param path Path to get.
	 * @return A boolean.
	 */
	boolean condition(@NotNull File file, @NotNull String path);
}
