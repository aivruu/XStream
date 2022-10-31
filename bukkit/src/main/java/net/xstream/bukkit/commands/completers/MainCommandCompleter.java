package net.xstream.bukkit.commands.completers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class MainCommandCompleter implements TabCompleter {
	private final List<String> commandArgs;
	private final List<String> reloadArgs;
	
	public MainCommandCompleter() {
		this.commandArgs = new ArrayList<>();
		this.reloadArgs = new ArrayList<>();
	}
	
	@Override
	public @Nullable List<String> onTabComplete(
		 @NotNull CommandSender sender,
		 @NotNull Command command,
		 @NotNull String label,
		 @NotNull String[] args
	) {
		if (this.commandArgs.isEmpty()) {
			this.commandArgs.add("help");
			this.commandArgs.add("reload");
		}
		
		if (this.reloadArgs.isEmpty()) {
			this.reloadArgs.add("config");
			this.reloadArgs.add("messages");
		}
		
		final List<String> results = new ArrayList<>();
		
		if (args.length == 1) {
			for (String result : this.commandArgs) {
				if (result.toLowerCase().startsWith(args[0].toLowerCase())) results.add(result);
			}
			return results;
		}
		
		if (args[0].equals("reload") && args.length == 2) {
			for (String result : this.reloadArgs) {
				if (result.toLowerCase().startsWith(args[1].toLowerCase())) results.add(result);
			}
			return results;
		}
		return null;
	}
}
