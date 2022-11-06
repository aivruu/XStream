package net.xstream.plugin.commands;

import com.cryptomorin.xseries.XSound;
import net.xconfig.bukkit.config.BukkitConfigurationHandler;
import net.xconfig.enums.Action;
import net.xstream.plugin.XStream;
import net.xstream.plugin.enums.Permission;
import net.xstream.plugin.utils.TextUtils;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class MainCommand implements CommandExecutor {
	private final BukkitConfigurationHandler configurationHandler;
	private final Sound permSound;
	private final Sound reloadSound;
	private final int volume;
	
	public MainCommand(@NotNull BukkitConfigurationHandler configurationHandler) {
		this.configurationHandler = Objects.requireNonNull(configurationHandler,
			 "The BukkitConfigurationHandler object is null.");
		this.permSound = XSound.valueOf(this.configurationHandler.text("config.yml", "config.sounds.no-perm")).parseSound();
		this.reloadSound = XSound.valueOf(this.configurationHandler.text("config.yml", "config.sounds.reload")).parseSound();
		this.volume = this.configurationHandler.number("config.yml", "config.sounds.volume-level");
	}
	
	@Override
	public boolean onCommand(
		 @NotNull CommandSender sender,
		 @NotNull Command command,
		 @NotNull String label,
		 @NotNull String[] args
	) {
		final String version = XStream.instance().release;
		final String prefix = this.configurationHandler.text("config.yml", "config.prefix");
		
		if (sender instanceof ConsoleCommandSender) {
			if (args.length == 0) {
				sender.sendMessage(TextUtils.colorize(prefix + " &fExecutes the &e/xs help &fcommand to "
					 + "view the commands."));
				sender.sendMessage(TextUtils.colorize(prefix + " &fDeveloped by &aInitSync &7- &b" + version));
				return false;
			}
			
			switch (args[0]) {
				default:
					sender.sendMessage(TextUtils.colorize(this.configurationHandler
						 .text("messages.yml", "messages.no-command")
						 .replace("<prefix>", prefix)));
					break;
				case "help":
					this.configurationHandler
						 .textList("messages.yml", "messages.help")
						 .forEach(message -> sender.sendMessage(TextUtils.colorize(message.replace("<release>", version))));
					break;
				case "reload":
					if (args.length == 1) {
						this.configurationHandler.doSomething("config.yml",
							 Action.RELOAD,
							 null,
							 null);
						this.configurationHandler.doSomething("messages.yml",
							 Action.RELOAD,
							 null,
							 null);
						sender.sendMessage(TextUtils.colorize(this.configurationHandler
							 .text("messages.yml", "messages.reload-all")
							 .replace("<prefix>", prefix)));
						break;
					}
					
					switch (args[1]) {
						default:
							sender.sendMessage(TextUtils.colorize(this.configurationHandler
								 .text("messages.yml", "messages.no-file")
								 .replace("<prefix>", prefix)));
							break;
						case "config":
							this.configurationHandler.doSomething("config.yml",
								 Action.RELOAD,
								 null,
								 null);
							sender.sendMessage(TextUtils.colorize(this.configurationHandler
								 .text("messages.yml", "messages.reload-config")
								 .replace("<prefix>", prefix)));
							break;
						case "messages":
							this.configurationHandler.doSomething("messages.yml",
								 Action.RELOAD,
								 null,
								 null);
							sender.sendMessage(TextUtils.colorize(this.configurationHandler
								 .text("messages.yml", "messages.reload-messages")
								 .replace("<prefix>", prefix)));
							break;
					}
					break;
			}
			return false;
		}
		
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			
			if (args.length == 0) {
				player.sendMessage(TextUtils.colorize(prefix + " &fExecutes the &e/xs help &fcommand to "
					 + "view the commands."));
				player.sendMessage(TextUtils.colorize(prefix + " &fDeveloped by &aInitSync &7- &b" + XStream.instance().release));
				return false;
			}
			
			switch (args[0]) {
				default:
					player.sendMessage(TextUtils.colorize(this.configurationHandler
						 .text("messages.yml", "messages.no-command")
						 .replace("<prefix>", prefix)));
					break;
				case "help":
					if (player.hasPermission(Permission.HELP_CMD.getPerm())) {
						this.configurationHandler
							 .textList("messages.yml", "messages.help")
							 .forEach(message -> player.sendMessage(TextUtils.colorize(message.replace("<release>", version))));
					} else {
						player.playSound(player.getLocation(),
							 this.permSound,
							 this.volume,
							 this.volume);
						player.sendMessage(TextUtils.colorize(this.configurationHandler
							 .text("messages.yml", "messages.no-perm")
							 .replace("<prefix>", prefix)));
					}
					break;
				case "reload":
					if (player.hasPermission(Permission.RELOAD_CMD.getPerm())) {
						if (args.length == 1) {
							this.configurationHandler.doSomething("config.yml",
								 Action.RELOAD,
								 null,
								 null);
							this.configurationHandler.doSomething("messages.yml",
								 Action.RELOAD,
								 null,
								 null);
							
							player.playSound(player.getLocation(),
								 this.reloadSound,
								 this.volume,
								 this.volume);
							player.sendMessage(TextUtils.colorize(this.configurationHandler
								 .text("messages.yml", "messages.reload-all")
								 .replace("<prefix>", prefix)));
							break;
						}
						
						switch (args[1]) {
							default:
								player.sendMessage(TextUtils.colorize(this.configurationHandler
									 .text("messages.yml", "messages.no-file")
									 .replace("<prefix>", prefix)));
								break;
							case "config":
								this.configurationHandler.doSomething("config.yml",
									 Action.RELOAD,
									 null,
									 null);
								
								player.playSound(player.getLocation(),
									 this.reloadSound,
									 this.volume,
									 this.volume);
								player.sendMessage(TextUtils.colorize(this.configurationHandler
									 .text("messages.yml", "messages.reload-config")
									 .replace("<prefix>", prefix)));
								break;
							case "messages":
								this.configurationHandler.doSomething("messages.yml",
									 Action.RELOAD,
									 null,
									 null);
								
								player.playSound(player.getLocation(),
									 this.reloadSound,
									 this.volume,
									 this.volume);
								player.sendMessage(TextUtils.colorize(this.configurationHandler
									 .text("messages.yml", "messages.reload-messages")
									 .replace("<prefix>", prefix)));
								break;
						}
					} else {
						player.playSound(player.getLocation(),
							 this.permSound,
							 this.volume,
							 this.volume);
						player.sendMessage(TextUtils.colorize(this.configurationHandler
							 .text("messages.yml", "messages.no-perm")
							 .replace("<prefix>", prefix)));
					}
					break;
			}
		}
		return false;
	}
}
