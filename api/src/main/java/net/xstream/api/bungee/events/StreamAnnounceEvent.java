package net.xstream.api.bungee.events;

import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

/**
 * Event for BungeeCord platforms that handles when a stream is announced.
 *
 * @author InitSync
 * @version 1.0.0
 * @since 1.0.0
 * @see net.md_5.bungee.api.plugin.Event
 * @see net.md_5.bungee.api.plugin.Cancellable
 */
public final class StreamAnnounceEvent extends Event implements Cancellable {
	private boolean cancelled;
	
	public StreamAnnounceEvent() {}
	
	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}
	
	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
