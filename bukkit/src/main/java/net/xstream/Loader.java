package net.xstream;

import net.xstream.api.AbstractLoader;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Loader that load and manage internally the plugin components.
 *
 * @author InitSync
 * @version 1.0.0
 * @since 1.0.0
 * @see net.xstream.api.AbstractLoader
 */
public final class Loader extends AbstractLoader {
	private final XStream plugin;
	
	public Loader(@NotNull XStream plugin) {
		this.plugin = Objects.requireNonNull(plugin, "The XStream instance is null.");
	}
	
	@Override
	public void enable() {
	
	}
	
	@Override
	public void disable() {
	
	}
}
