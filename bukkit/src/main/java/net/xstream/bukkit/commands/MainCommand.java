package net.xstream.bukkit.commands;

import com.cryptomorin.xseries.XSound;
import net.xconfig.bukkit.config.BukkitConfigurationHandler;
import net.xconfig.enums.Action;
import net.xconfig.enums.File;
import net.xstream.bukkit.XStream;
import net.xstream.bukkit.enums.Permission;
import net.xstream.bukkit.utils.TextUtils;
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
		this.permSound = XSound.valueOf(this.configurationHandler.text(File.CONFIG,
			 "config.sounds.no-perm",
			 null))
			 .parseSound();
		this.reloadSound = XSound.valueOf(this.configurationHandler.text(File.CONFIG,
					"config.sounds.reload",
					null))
			 .parseSound();
		this.volume = this.configurationHandler.number(File.CONFIG,
			 "config.sounds.volume-level",
			 null);
	}
	
	@Override
	public boolean onCommand(
		 @NotNull CommandSender sender,
		 @NotNull Command command,
		 @NotNull String label,
		 @NotNull String[] args
	) {
		final String prefix = this.configurationHandler.text(File.CONFIG, "config.prefix", null);
		
		if (sender instanceof ConsoleCommandSender) {
			if (args.length == 0) {
				sender.sendMessage(TextUtils.parse(prefix + " &fExecutes the &e/xs help &fcommand to "
					 + "view the commands."));
				sender.sendMessage(TextUtils.parse(prefix + "&fDeveloped by &aInitSync &7- &b" + XStream.instance().release));
				return false;
			}
			
			switch (args[0]) {
				default:
					sender.sendMessage(TextUtils.parse(this.configurationHandler
						 .text(File.CUSTOM,
								"messages.no-command",
								"messages.yml")
						 .replace("<prefix>", prefix)));
					break;
				case "help":
					this.configurationHandler
						 .textList(File.CUSTOM,
								"messages.help",
								"messages.yml")
						 .forEach(message -> sender.sendMessage(TextUtils.parse(message)));
					break;
				case "reload":
					if (args.length == 1) {
						this.configurationHandler.doSomething(File.CONFIG,
							 Action.RELOAD,
							 null,
							 null,
							 null);
						this.configurationHandler.doSomething(File.CUSTOM,
							 Action.RELOAD,
							 null,
							 null,
							 "messages.yml");
						sender.sendMessage(TextUtils.parse(this.configurationHandler
							 .text(File.CUSTOM,
									"messages.reload-all",
									"messages.yml")
							 .replace("<prefix>", prefix)));
						break;
					}
					
					switch (args[1]) {
						default:
							sender.sendMessage(TextUtils.parse(this.configurationHandler
								 .text(File.CUSTOM,
										"messages.no-file",
										"messages.yml")
								 .replace("<prefix>", prefix)));
							break;
						case "config":
							this.configurationHandler.doSomething(File.CONFIG,
								 Action.RELOAD,
								 null,
								 null,
								 null);
							sender.sendMessage(TextUtils.parse(this.configurationHandler
								 .text(File.CUSTOM,
										"messages.reload-config",
										"messages.yml")
								 .replace("<prefix>", prefix)));
							break;
						case "messages":
							this.configurationHandler.doSomething(File.CUSTOM,
								 Action.RELOAD,
								 null,
								 null,
								 "messages.yml");
							sender.sendMessage(TextUtils.parse(this.configurationHandler
								 .text(File.CUSTOM,
										"messages.reload-messages",
										"messages.yml")
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
				player.sendMessage(TextUtils.parse(prefix + " &fExecutes the &e/xs help &fcommand to "
					 + "view the commands."));
				player.sendMessage(TextUtils.parse(prefix + "&fDeveloped by &aInitSync &7- &b" + XStream.instance().release));
				return false;
			}
			
			switch (args[0]) {
				default:
					player.sendMessage(TextUtils.parse(this.configurationHandler
						 .text(File.CUSTOM,
								"messages.no-command",
								"messages.yml")
						 .replace("<prefix>", prefix)));
					break;
				case "help":
					if (player.hasPermission(Permission.HELP_CMD.getPerm())) {
						this.configurationHandler
							 .textList(File.CUSTOM,
									"messages.help",
									"messages.yml")
							 .forEach(message -> player.sendMessage(TextUtils.parse(message)));
					} else {
						player.playSound(player.getLocation(),
							 this.permSound,
							 this.volume,
							 this.volume);
						player.sendMessage(TextUtils.parse(this.configurationHandler
							 .text(File.CUSTOM,
									"messages.no-perm",
									"messages.yml")
							 .replace("<prefix>", prefix)));
					}
					break;
				case "reload":
					if (player.hasPermission(Permission.RELOAD_CMD.getPerm())) {
						if (args.length == 1) {
							this.configurationHandler.doSomething(File.CONFIG,
								 Action.RELOAD,
								 null,
								 null,
								 null);
							this.configurationHandler.doSomething(File.CUSTOM,
								 Action.RELOAD,
								 null,
								 null,
								 "messages.yml");
							
							player.playSound(player.getLocation(),
								 this.reloadSound,
								 this.volume,
								 this.volume);
							player.sendMessage(TextUtils.parse(this.configurationHandler
								 .text(File.CUSTOM,
										"messages.reload-all",
										"messages.yml")
								 .replace("<prefix>", prefix)));
							break;
						}
						
						switch (args[1]) {
							default:
								player.sendMessage(TextUtils.parse(this.configurationHandler
									 .text(File.CUSTOM,
											"messages.no-file",
											"messages.yml")
									 .replace("<prefix>", prefix)));
								break;
							case "config":
								this.configurationHandler.doSomething(File.CONFIG,
									 Action.RELOAD,
									 null,
									 null,
									 null);
								
								player.playSound(player.getLocation(),
									 this.reloadSound,
									 this.volume,
									 this.volume);
								player.sendMessage(TextUtils.parse(this.configurationHandler
									 .text(File.CUSTOM,
											"messages.reload-config",
											"messages.yml")
									 .replace("<prefix>", prefix)));
								break;
							case "messages":
								this.configurationHandler.doSomething(File.CUSTOM,
									 Action.RELOAD,
									 null,
									 null,
									 "messages.yml");
								
								player.playSound(player.getLocation(),
									 this.reloadSound,
									 this.volume,
									 this.volume);
								player.sendMessage(TextUtils.parse(this.configurationHandler
									 .text(File.CUSTOM,
											"messages.reload-messages",
											"messages.yml")
									 .replace("<prefix>", prefix)));
								break;
						}
					} else {
						player.playSound(player.getLocation(),
							 this.permSound,
							 this.volume,
							 this.volume);
						player.sendMessage(TextUtils.parse(this.configurationHandler
							 .text(File.CUSTOM,
									"messages.no-perm",
									"messages.yml")
							 .replace("<prefix>", prefix)));
					}
					break;
			}
		}
		return false;
	}
}
