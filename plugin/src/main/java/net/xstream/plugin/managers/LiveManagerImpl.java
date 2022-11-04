package net.xstream.plugin.managers;

import net.xconfig.bukkit.config.BukkitConfigurationHandler;
import net.xconfig.enums.File;
import net.xstream.api.managers.LiveManager;
import net.xstream.plugin.XStream;
import net.xstream.plugin.utils.TextUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class LiveManagerImpl implements LiveManager {
	private final Map<UUID, String> streams;
	private final Map<UUID, BukkitTask> tasks;
	private final BukkitConfigurationHandler configurationHandler;
	
	public LiveManagerImpl(@NotNull BukkitConfigurationHandler configurationHandler) {
		this.streams = new HashMap<>();
		this.tasks = new HashMap<>();
		this.configurationHandler = Objects.requireNonNull(configurationHandler,
			 "The BukkitConfigurationHandler is null.");
	}
	
	@Override
	public @NotNull Map<UUID, String> streams() {
		return this.streams;
	}
	
	@Override
	public @NotNull Map<UUID, BukkitTask> tasks() {
		return this.tasks;
	}
	
	@Override
	public boolean isStreaming(@NotNull UUID uuid) {
		Objects.requireNonNull(uuid, "The uuid is null.");
		
		return this.streams.containsKey(uuid) && this.tasks.containsKey(uuid);
	}
	
	/**
	 * Sets the link of stream.
	 *
	 * @param uuid UUID of streamer.
	 * @param link Stream link.
	 */
	@Override
	public void prepare(@NotNull UUID uuid, @NotNull String link) {
		Objects.requireNonNull(uuid, "The uuid is null.");
		Validate.notEmpty(link, "The link is empty.");
		
		this.streams.put(uuid, link);
	}
	
	/**
	 * Start a task to announce the stream.
	 *
	 * @param uuid
	 */
	@Override
	public void announce(@NotNull UUID uuid) {
		Objects.requireNonNull(uuid, "The uuid is null.");
		
		final BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(XStream.instance(), () -> {
			Bukkit.getOnlinePlayers().forEach(player -> {
				this.configurationHandler
					.textList(File.CUSTOM,
						  "messages.announce-message",
						  "messages.yml")
					.forEach(msg -> {
						player.sendMessage(TextUtils.parse(msg.replace("<player_name>",
							 Bukkit.getPlayer(uuid).getName())
							 .replace("<stream_url>", this.streams.get(uuid))));
					});
			});
		}, 1L, this.configurationHandler.number(File.CONFIG,
			 "config.announces-delay",
			 null));
		
		this.tasks.put(uuid, task);
	}
	
	/**
	 * Sets as offline the stream.
	 *
	 * @param uuid UUID of streamer.
	 */
	@Override
	public void offline(@NotNull UUID uuid) {
		Objects.requireNonNull(uuid, "The uuid is null.");
		
		this.streams.remove(uuid);
		this.tasks
			 .remove(uuid)
			 .cancel();
	}
}
