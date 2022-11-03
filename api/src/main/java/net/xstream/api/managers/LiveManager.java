package net.xstream.api.managers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public interface LiveManager {
	@NotNull Map<UUID, String> streams();
	
	@NotNull Map<UUID, BukkitTask> tasks();
	
	/**
	 * Checks if the streamer is active yet.
	 *
	 * @param uuid UUID of streamer.
	 */
	boolean isStreaming(@NotNull UUID uuid);
	
	/**
	 * Sets the link of stream.
	 *
	 * @param uuid UUID of streamer.
	 * @param link Stream link.
	 */
	void prepare(@NotNull UUID uuid, @NotNull String link);
	
	/**
	 * Start a task to announce the stream.
	 *
	 * @param uuid UUID of streamer.
	 */
	void announce(@NotNull UUID uuid);
	
	/**
	 * Sets as offline the stream.
	 *
	 * @param uuid UUID of streamer.
	 */
	void offline(@NotNull UUID uuid);
	
	/**
	 * Opens the Live Manager Menu to player.
	 *
	 * @param player Player to open the menu.
	 */
	void openLiveManager(@NotNull Player player);
}
