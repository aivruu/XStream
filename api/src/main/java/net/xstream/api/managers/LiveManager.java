package net.xstream.api.managers;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface LiveManager {
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
	 * @param link Stream link.
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
