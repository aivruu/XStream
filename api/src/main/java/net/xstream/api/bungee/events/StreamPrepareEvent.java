package net.xstream.api.bungee.events;

import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

/**
 * BungeeCord Event called when the streamer sets the link of stream.
 *
 * @author InitSync
 * @version 1.0.0
 * @since 1.0.0
 * @see net.md_5.bungee.api.plugin.Event
 * @see net.md_5.bungee.api.plugin.Cancellable
 */
public final class StreamPrepareEvent extends Event implements Cancellable {
	private final String link;
	
	private boolean cancelled;
	
	public StreamPrepareEvent(@NotNull String link) {
		Validate.notEmpty(link, "The link is empty.");
		this.link = link;
	}
	
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
}
