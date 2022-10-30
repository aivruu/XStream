package net.xstream;

import net.xconfig.config.ConfigurationHandler;
import net.xconfig.config.ConfigurationModel;
import net.xconfig.services.ConfigurationService;
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
	
	private ConfigurationModel configurationManager;
	private ConfigurationHandler configurationHandler;
	
	public Loader(@NotNull XStream plugin) {
		this.plugin = Objects.requireNonNull(plugin, "The XStream instance is null.");
	}
	
	/**
	 * Returns the ConfigurationModel object.
	 *
	 * @return A ConfigurationModel object.
	 */
	public @NotNull ConfigurationModel configurationManager() {
		if (this.configurationManager == null) {
			throw new IllegalStateException("Cannot access to the ConfigurationModel object.");
		}
		return this.configurationManager;
	}
	
	/**
	 * Returns the ConfigurationHandler object, if it is null, will be throws an
	 * IllegalStateException.
	 *
	 * @return A ConfigurationHandler object.
	 */
	public @NotNull ConfigurationHandler configurationHandler() {
		if (this.configurationHandler == null) {
			throw new IllegalStateException("Cannot access to the ConfigurationHandler instance.");
		}
		return this.configurationHandler;
	}
	
	@Override
	public void enable() {
		this.configurationManager = ConfigurationService.manager(this.plugin);
		this.configurationManager.create("", "config.yml");
		this.configurationManager.create("", "messages.yml");
		this.configurationHandler = ConfigurationService.handler(this.configurationManager);
	}
	
	@Override
	public void disable() {
		if (this.configurationManager != null) this.configurationManager = null;
		if (this.configurationHandler != null) this.configurationHandler = null;
	}
}
