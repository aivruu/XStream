package net.xstream.plugin.managers;

import net.xstream.plugin.utils.LogPrinter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * A class that checks if the plugin has a new version available for update.
 *
 * @author InitSync
 * @version 1.0.2
 * @since 1.0.2
 */
public final class UpdateChecker {
	private final JavaPlugin plugin;
	private final int resourceId;
	
	public UpdateChecker(@NotNull JavaPlugin plugin, int resourceId) {
		this.plugin = Objects.requireNonNull(plugin, "The JavaPlugin instance is null.");
		this.resourceId = resourceId;
	}
	
	public void getVersion(@NotNull Consumer<String> consumer) {
		Objects.requireNonNull(consumer, "The consumer is null.");
		
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId)
				.openStream();
				final Scanner scanner = new Scanner(inputStream)
			) {
				if (scanner.hasNext()) consumer.accept(scanner.next());
			} catch (IOException exception) {
				LogPrinter.error("Unable to check for updates available: " + exception.getMessage());
			}
		});
	}
}
