package net.xstream.api.events;

import org.apache.commons.lang.Validate;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Bukkit Event called when the streamer sets the link of stream.
 *
 * @author InitSync
 * @version 1.0.1
 * @since 1.0.0
 * @see org.bukkit.event.Event
 * @see org.bukkit.event.Cancellable
 */
public final class StreamPrepareEvent extends Event implements Cancellable {
	private final HandlerList handlers;
	private final String link;
	
	private boolean cancelled;
	
	public StreamPrepareEvent(@NotNull String link) {
		this.handlers = new HandlerList();
		
		Validate.notEmpty(link, "The link is empty.");
		this.link = link;
	}
	
	/**
	 * Returns the link used at this event.
	 *
	 * @return A String object.
	 */
	public @NotNull String getLink() {
		return this.link;
	}
	
	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
	
	@Override
	public @NotNull HandlerList getHandlers() {
		return this.handlers;
	}
}
