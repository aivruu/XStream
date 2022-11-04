package net.xstream.plugin;

import net.xconfig.bukkit.config.BukkitConfigurationHandler;
import net.xconfig.bukkit.config.BukkitConfigurationModel;
import net.xconfig.services.ConfigurationService;
import net.xstream.api.AbstractLoader;
import net.xstream.api.managers.LiveManager;
import net.xstream.plugin.commands.LiveCommand;
import net.xstream.plugin.commands.MainCommand;
import net.xstream.plugin.services.LoaderService;
import net.xstream.plugin.services.ManagerService;
import net.xstream.plugin.utils.LogPrinter;
import org.bukkit.Bukkit;
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
	
	private BukkitConfigurationModel configurationManager;
	private BukkitConfigurationHandler configurationHandler;
	private LiveManager liveManager;
	
	public Loader(@NotNull XStream plugin) {
		this.plugin = Objects.requireNonNull(plugin, "The XStream instance is null.");
		this.configurationManager = ConfigurationService.bukkitManager(this.plugin);
		this.configurationHandler = ConfigurationService.bukkitHandler(this.configurationManager);
		this.liveManager = ManagerService.liveManager(this.configurationHandler);
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
	public @NotNull LiveManager liveManager() {
		if (this.liveManager == null) {
			throw new IllegalStateException("Cannot access to BukkitLiveManager object.");
		}
		return this.liveManager;
	}
	
	@Override
	public void enable() {
		final long startTime = System.currentTimeMillis();
		
		this.configurationManager.create("",
			 "config.yml",
			 "messages.yml");
		this.configurationManager.load("config.yml", "messages.yml");
		
		LoaderService.commandLoader(this.plugin)
			 .command("xstream")
			 .executor(new MainCommand(this.configurationHandler))
			 .register()
			 .command("live")
			 .executor(new LiveCommand(this.configurationHandler, this.liveManager))
			 .register();
		
		LogPrinter.info("Started plugin successfully in '" + (System.currentTimeMillis() - startTime) + "'ms.",
			 "Running with [" + Bukkit.getVersion() + "-" + Bukkit.getBukkitVersion() + "]",
			 "Developed by InitSync | v" + this.plugin.release);
	}
	
	@Override
	public void disable() {
		LogPrinter.info("Disabling plugin...", "Developed by InitSync | v" + this.plugin.release);
		
		if (this.configurationManager != null) this.configurationManager = null;
		if (this.configurationHandler != null) this.configurationHandler = null;
	}
}
