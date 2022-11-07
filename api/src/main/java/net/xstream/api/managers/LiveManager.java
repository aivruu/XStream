package net.xstream.api.managers;

import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

/**
 * Interface Model for the Live Manager implementation.
 *
 * @author InitSync
 * @version 1.0.2
 * @since 1.0.0
 */
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
}
