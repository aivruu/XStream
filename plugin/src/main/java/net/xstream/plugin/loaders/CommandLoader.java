package net.xstream.plugin.loaders;

import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Loader for register commands rapidly.
 *
 * @author InitSync
 * @version 1.0.0
 * @since 1.0.0
 */
public final class CommandLoader {
	private CommandLoader() {}
	
	public static class Builder {
		private final JavaPlugin plugin;
		
		private String commandName;
		private CommandExecutor executor;
		private TabCompleter completer;
		
		public Builder(@NotNull JavaPlugin plugin) {
			this.plugin = Objects.requireNonNull(plugin, "The JavaPlugin instance is null.");
		}
		
		/**
		 * Sets a new String for the 'command' field.
		 *
		 * @param commandName Name for the command.
		 * @return this
		 */
		public Builder command(@NotNull String commandName) {
			Validate.notEmpty(commandName, "The command name is empty.");
			this.commandName = commandName;
			return this;
		}
		
		/**
		 * Sets a new CommandExecutor object for the 'executor' field.
		 *
		 * @param executor CommandExecutor object to set.
		 * @return this
		 */
		public Builder executor(@NotNull CommandExecutor executor) {
			this.executor = Objects.requireNonNull(executor, "The command executor is null.");
			return this;
		}
		
		/**
		 * Updates the 'completer' field with a new TabCompleter object.
		 *
		 * @param completer TabCompleter object to set.
		 * @return this
		 */
		public Builder completer(@NotNull TabCompleter completer) {
			this.completer = Objects.requireNonNull(completer, "The tab completer is null.");
			return this;
		}
		
		public Builder register() {
			if (this.commandName == null || this.commandName.isEmpty()) {
				throw new IllegalArgumentException("The command name can't be null or empty.");
			}
			
			if (this.executor == null) {
				throw new IllegalArgumentException("The command executor can't be null.");
			}
			
			final PluginCommand command = this.plugin.getCommand(this.commandName);
			assert command != null;
			command.setExecutor(this.executor);
			
			if (this.completer != null) command.setTabCompleter(this.completer);
			
			this.commandName = null;
			this.executor = null;
			this.completer = null;
			return this;
		}
	}
}
