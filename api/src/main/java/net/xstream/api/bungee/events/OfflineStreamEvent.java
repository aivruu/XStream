package net.xstream.api.bungee.events;

import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Event for BungeeCord platforms called when the streamer sets offline the stream with /live offline.
 *
 * @author InitSync
 * @version 1.0.0
 * @since 1.0.0
 * @see net.md_5.bungee.api.plugin.Event
 * @see net.md_5.bungee.api.plugin.Cancellable
 */
public final class OfflineStreamEvent extends Event implements Cancellable {
	private final Player player;
	
	private boolean cancelled;
	
	public OfflineStreamEvent(@NotNull Player player) {
		this.player = Objects.requireNonNull(player, "The player is null.");
	}
	
	public @NotNull Player player() {
		return this.player;
	}
	
	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}
	
	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
