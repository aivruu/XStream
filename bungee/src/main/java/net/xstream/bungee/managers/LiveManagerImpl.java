package net.xstream.bungee.managers;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.xconfig.bungee.config.BungeeConfigurationHandler;
import net.xconfig.enums.File;
import net.xstream.api.managers.BungeeLiveManager;
import net.xstream.bungee.XStream;
import net.xstream.bungee.utils.Utils;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class LiveManagerImpl implements BungeeLiveManager {
	private final XStream plugin;
	private final Map<UUID, String> streams;
	private final Map<UUID, ScheduledTask> tasks;
	private final BungeeConfigurationHandler configurationHandler;
	
	public LiveManagerImpl(
		 @NotNull XStream plugin,
		 @NotNull BungeeConfigurationHandler configurationHandler
	) {
		this.plugin = Objects.requireNonNull(plugin, "The XStream instance is null.");
		this.streams = new HashMap<>();
		this.tasks = new HashMap<>();
		this.configurationHandler = Objects.requireNonNull(configurationHandler,
			 "The BungeeConfigurationHandler object is null.");
	}
	
	@Override
	public @NotNull Map<UUID, String> streams() {
		return this.streams;
	}
	
	@Override
	public @NotNull Map<UUID, ScheduledTask> tasks() {
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
	 * @param uuid UUID of streamer.
	 */
	@Override
	public void announce(@NotNull UUID uuid) {
		Objects.requireNonNull(uuid, "The uuid is null.");
		
		final ScheduledTask task = this.plugin
			 .getProxy()
			 .getScheduler()
			 .schedule(this.plugin, () -> {
				 this.plugin
					  .getProxy()
					  .broadcast(TextComponent.fromLegacyText(Utils.parse(this.configurationHandler
						   .text(File.CUSTOM,
							    "messages.announce-message",
							    "messages.yml")
						   .replace("<player_name>", this.plugin
							    .getProxy()
							    .getPlayer(uuid)
							    .getName())
						   .replace("<stream_url>", this.streams.get(uuid)))));
			 }, 1L, this.configurationHandler.number(File.CONFIG,
				  "config.announces-delay",
				  null),
				  TimeUnit.SECONDS);
		
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
