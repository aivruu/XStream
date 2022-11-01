package net.xstream.api.managers;

import net.md_5.bungee.api.scheduler.ScheduledTask;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public interface BungeeLiveManager {
	@NotNull Map<UUID, String> streams();
	
	@NotNull Map<UUID, ScheduledTask> tasks();
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
