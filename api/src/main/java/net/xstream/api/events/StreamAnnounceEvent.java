package net.xstream.api.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event for Bukkit servers that handles when a stream is announced.
 *
 * @author InitSync
 * @version 1.0.0
 * @since 1.0.0
 * @see org.bukkit.event.Event
 * @see org.bukkit.event.Cancellable
 */
public final class StreamAnnounceEvent extends Event implements Cancellable {
	private final HandlerList handlers;
	
	private boolean cancelled;
	
	public StreamAnnounceEvent() {
		this.handlers = new HandlerList();
	}
	
	@Override
	public @NotNull HandlerList getHandlers() {
		return this.handlers;
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
