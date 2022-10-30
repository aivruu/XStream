package net.xstream.api.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Model for the ConfigurationManager.
 *
 * @author InitSync
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ManagerConfig {
	/**
	 * Returns a FileConfiguration object that represents the file specified.
	 *
	 * @param fileName Name of file.
	 * @return A FileConfiguration object.
	 */
	@Nullable FileConfiguration file(@NotNull String fileName);
	
	/**
	 * Creates a new file with a folder.
	 *
	 * @param folderName Name of the folder, can be empty.
	 * @param fileName Name of file.
	 */
	void create(@NotNull String folderName, @NotNull String fileName);
	
	/**
	 * Loads a existing file.
	 *
	 * @param fileName Name of file.
	 */
	void load(@NotNull String fileName);
	
	/**
	 * Reload a existing file.
	 *
	 * @param fileName Name of file.
	 */
	void reload(@NotNull String fileName);
	
	/**
	 * Save an existing file.
	 *
	 * @param fileName Name of file.
	 */
	void save(@NotNull String fileName);
	
	/**
	 * Save the default content of file.
	 *
	 * @param folderName Name of the folder of file.
	 * @param fileName Name of file.
	 */
	void save(@NotNull String folderName, @NotNull String fileName);
}
