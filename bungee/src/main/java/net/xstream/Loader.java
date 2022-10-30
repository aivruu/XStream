package net.xstream;

import net.xconfig.bungee.config.BungeeConfigurationHandler;
import net.xconfig.bungee.config.BungeeConfigurationModel;
import net.xconfig.services.ConfigurationService;
import net.xstream.api.AbstractLoader;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Loader extends AbstractLoader {
	private final XStream plugin;
	
	private BungeeConfigurationModel configurationManager;
	private BungeeConfigurationHandler configurationHandler;
	
	public Loader(@NotNull XStream plugin) {
		this.plugin = Objects.requireNonNull(plugin, "The XStream instance is null.");
	}
	
	/**
	 * Returns the BungeeConfigurationModel object.
	 *
	 * @return A BungeeConfigurationModel object.
	 */
	public @NotNull BungeeConfigurationModel configurationManager() {
		if (this.configurationManager == null) {
			throw new IllegalStateException("Cannot access to the BukkitConfigurationModel object.");
		}
		return this.configurationManager;
	}
	
	/**
	 * Returns the BungeeConfigurationHandler object, if it is null, will be throws an
	 * IllegalStateException.
	 *
	 * @return A BungeeConfigurationHandler object.
	 */
	public @NotNull BungeeConfigurationHandler configurationHandler() {
		if (this.configurationHandler == null) {
			throw new IllegalStateException("Cannot access to the BukkitConfigurationHandler instance.");
		}
		return this.configurationHandler;
	}
	
	@Override
	public void enable() {
		this.configurationManager = ConfigurationService.bungeeManager(this.plugin);
		this.configurationManager.create("",
			 "bungee_config.yml",
			 "messages.yml");
		this.configurationManager.load(
			 "bungee_config.yml",
			 "messages.yml");
		this.configurationHandler = ConfigurationService.bungeeHandler(this.configurationManager);
	}
	
	@Override
	public void disable() {
		if (this.configurationManager != null) this.configurationManager = null;
		if (this.configurationHandler != null) this.configurationHandler = null;
	}
}
