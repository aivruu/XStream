package net.xstream.bukkit;

import net.xconfig.bukkit.config.BukkitConfigurationHandler;
import net.xconfig.bukkit.config.BukkitConfigurationModel;
import net.xconfig.services.ConfigurationService;
import net.xstream.api.AbstractLoader;
import net.xstream.api.managers.BukkitLiveManager;
import net.xstream.bukkit.commands.LiveCommand;
import net.xstream.bukkit.commands.MainCommand;
import net.xstream.bukkit.services.LoaderService;
import net.xstream.bukkit.services.ManagerService;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import team.unnamed.gui.menu.listener.InventoryClickListener;
import team.unnamed.gui.menu.listener.InventoryCloseListener;
import team.unnamed.gui.menu.listener.InventoryOpenListener;

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
	private final PluginManager pluginManager;
	
	private BukkitConfigurationModel configurationManager;
	private BukkitConfigurationHandler configurationHandler;
	private BukkitLiveManager liveManager;
	
	public Loader(@NotNull XStream plugin) {
		this.plugin = Objects.requireNonNull(plugin, "The XStream instance is null.");
		this.pluginManager = this.plugin
			 .getServer()
			 .getPluginManager();
	}
	
	/**
	 * Returns the BukkitConfigurationModel object.
	 *
	 * @return A BukkitConfigurationModel object.
	 */
	public @NotNull BukkitConfigurationModel configurationManager() {
		if (this.configurationManager == null) {
			throw new IllegalStateException("Cannot access to the BukkitConfigurationModel object.");
		}
		return this.configurationManager;
	}
	
	/**
	 * Returns the BukkitConfigurationHandler object, if it is null, will be throws an
	 * IllegalStateException.
	 *
	 * @return A BukkitConfigurationHandler object.
	 */
	public @NotNull BukkitConfigurationHandler configurationHandler() {
		if (this.configurationHandler == null) {
			throw new IllegalStateException("Cannot access to the BukkitConfigurationHandler instance.");
		}
		return this.configurationHandler;
	}
	
	/**
	 * Returns the BukkitLiveManager object.
	 *
	 * @return A BukkitLiveManager object.
	 */
	public @NotNull BukkitLiveManager liveManager() {
		if (this.liveManager == null) {
			throw new IllegalStateException("Cannot access to BukkitLiveManager object.");
		}
		return this.liveManager;
	}
	
	@Override
	public void enable() {
		this.configurationManager = ConfigurationService.bukkitManager(this.plugin);
		this.configurationManager.create("",
			 "config.yml",
			 "messages.yml");
		this.configurationManager.load("config.yml", "messages.yml");
		this.configurationHandler = ConfigurationService.bukkitHandler(this.configurationManager);
		
		this.liveManager = ManagerService.bukkitLiveManager(this.configurationHandler);
		
		this.pluginManager.registerEvents(new InventoryClickListener(), this.plugin);
		this.pluginManager.registerEvents(new InventoryCloseListener(this.plugin), this.plugin);
		this.pluginManager.registerEvents(new InventoryOpenListener(), this.plugin);
		
		LoaderService.commandLoader(this.plugin)
			 .command("xstream")
			 .executor(new MainCommand(this.configurationHandler))
			 .register()
			 .command("live")
			 .executor(new LiveCommand(this.configurationHandler, this.liveManager))
			 .register();
	}
	
	@Override
	public void disable() {
		if (this.configurationManager != null) this.configurationManager = null;
		if (this.configurationHandler != null) this.configurationHandler = null;
	}
}
