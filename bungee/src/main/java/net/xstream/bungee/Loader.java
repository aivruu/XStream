package net.xstream.bungee;

import net.xconfig.bungee.config.BungeeConfigurationHandler;
import net.xconfig.bungee.config.BungeeConfigurationModel;
import net.xconfig.services.ConfigurationService;
import net.xstream.api.AbstractLoader;
import net.xstream.api.managers.BungeeLiveManager;
import net.xstream.bungee.commands.LiveCommand;
import net.xstream.bungee.services.LoaderService;
import net.xstream.bungee.services.ManagerService;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Loader extends AbstractLoader {
	private final XStream plugin;
	
	private BungeeConfigurationModel configurationManager;
	private BungeeConfigurationHandler configurationHandler;
	private BungeeLiveManager liveManager;
	
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
	
	/**
	 * Returns the BungeeLiveManager object.
	 *
	 * @return A BungeeLiveManager object.
	 */
	public @NotNull BungeeLiveManager liveManager() {
		if (this.liveManager == null) {
			throw new IllegalStateException("Cannot access to BungeeLiveManager object.");
		}
		return this.liveManager;
	}
	
	@Override
	public void enable() {
		this.configurationManager = ConfigurationService.bungeeManager(this.plugin);
		this.configurationManager.create("",
			 "config.yml",
			 "messages.yml");
		this.configurationManager.load(
			 "config.yml",
			 "messages.yml");
		this.configurationHandler = ConfigurationService.bungeeHandler(this.configurationManager);
		
		this.liveManager = ManagerService.bungeeLiveManager(this.plugin, this.configurationHandler);
		
		LoaderService.commandLoader(this.plugin)
			 .command(new LiveCommand(this.plugin, this.configurationHandler, this.liveManager))
			 .register();
	}
	
	@Override
	public void disable() {
		if (this.configurationManager != null) this.configurationManager = null;
		if (this.configurationHandler != null) this.configurationHandler = null;
	}
}
