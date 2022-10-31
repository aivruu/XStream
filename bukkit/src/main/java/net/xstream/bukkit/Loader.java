package net.xstream.bukkit;

import net.xconfig.bukkit.config.BukkitConfigurationHandler;
import net.xconfig.bukkit.config.BukkitConfigurationModel;
import net.xconfig.services.ConfigurationService;
import net.xstream.api.AbstractLoader;
import net.xstream.bukkit.commands.MainCommand;
import net.xstream.bukkit.commands.completers.MainCommandCompleter;
import net.xstream.bukkit.services.LoaderService;
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
	
	public Loader(@NotNull XStream plugin) {
		this.plugin = Objects.requireNonNull(plugin, "The XStream instance is null.");
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
	
	@Override
	public void enable() {
		this.configurationManager = ConfigurationService.bukkitManager(this.plugin);
		this.configurationManager.create("",
			 "config.yml",
			 "messages.yml");
		this.configurationManager.load("config.yml", "messages.yml");
		this.configurationHandler = ConfigurationService.bukkitHandler(this.configurationManager);
		
		LoaderService.commandLoader(this.plugin)
			 .command("xstream")
			 .executor(new MainCommand(this.configurationHandler))
			 .completer(new MainCommandCompleter())
			 .register();
	}
	
	@Override
	public void disable() {
		if (this.configurationManager != null) this.configurationManager = null;
		if (this.configurationHandler != null) this.configurationHandler = null;
	}
}
