package net.xstream.bungee.loaders;

import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class CommandLoader {
	private CommandLoader() {}
	
	public static class Builder {
		private final Plugin plugin;
		
		private Command command;
		
		public Builder(@NotNull Plugin plugin) {
			this.plugin = Objects.requireNonNull(plugin, "The Plugin instance is null.");
		}
		
		/**
		 * Sets a new Command object for the 'command' field.
		 *
		 * @param command Command object to set.
		 * @return this
		 */
		public Builder command(@NotNull Command command) {
			this.command = Objects.requireNonNull(command, "The command is null.");
			return this;
		}
		
		public Builder register() {
			if (this.command == null) {
				throw new IllegalArgumentException("The command can't be null.");
			}
			
			this.plugin
				 .getProxy()
				 .getPluginManager()
				 .registerCommand(this.plugin, this.command);
			
			this.command = null;
			return this;
		}
	}
}
