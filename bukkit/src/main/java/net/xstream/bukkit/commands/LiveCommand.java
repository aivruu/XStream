package net.xstream.bukkit.commands;

import com.cryptomorin.xseries.XSound;
import net.xconfig.bukkit.config.BukkitConfigurationHandler;
import net.xconfig.enums.File;
import net.xstream.api.managers.LiveManager;
import net.xstream.bukkit.enums.Permission;
import net.xstream.bukkit.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public final class LiveCommand implements CommandExecutor {
	private final BukkitConfigurationHandler configurationHandler;
	private final LiveManager liveManager;
	
	public LiveCommand(
		 @NotNull BukkitConfigurationHandler configurationHandler,
		 @NotNull LiveManager liveManager
	) {
		this.configurationHandler = Objects.requireNonNull(configurationHandler,
			 "The BukkitConfigurationHandler object is null.");
		this.liveManager = Objects.requireNonNull(liveManager, "The LiveManager object is null.");
	}
	
	@Override
	public boolean onCommand(
		 @NotNull CommandSender sender,
		 @NotNull Command command,
		 @NotNull String label,
		 @NotNull String[] args
	) {
		final String prefix = this.configurationHandler.text(File.CONFIG,
			 "config.prefix",
			 null);
		
		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage(TextUtils.parse(this.configurationHandler
				 .text(File.CUSTOM,
					  "messages.no-console",
					  "messages.yml")
				 .replace("<prefix>", prefix)));
			return false;
		}
		
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			final UUID playerId = player.getUniqueId();
			
			if (player.hasPermission(Permission.LIVE_CMD.getPerm())) {
				if (args.length == 0) {
					player.sendMessage(TextUtils.parse(this.configurationHandler
						 .text(File.CUSTOM,
							  "messages.live-usage",
							  "messages.yml")
						 .replace("<prefix>", prefix)));
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
					case "offline":
						this.liveManager.offline(playerId);
						player.sendMessage(TextUtils.parse(this.configurationHandler
							 .text(File.CUSTOM,
									"messages.live-offline",
								  "messages.yml")
							 .replace("<prefix>", prefix)));
						Bukkit.getOnlinePlayers().forEach(connected -> {
							connected.sendMessage(TextUtils.parse(this.configurationHandler
								 .text(File.CUSTOM,
									  "messages.announce-offline",
									  "messages.yml")
								 .replace("<player_name>", player.getName())));
						});
						break;
					case "url":
						if (args.length == 1) {
							player.sendMessage(TextUtils.parse(this.configurationHandler
								 .text(File.CUSTOM,
										"messages.live-usage",
										"messages.yml")
								 .replace("<prefix>", prefix)));
							return false;
						}
						
						this.liveManager.prepare(playerId, args[1]);
						player.sendMessage(TextUtils.parse(this.configurationHandler
							 .text(File.CUSTOM,
								  "messages.live-url-set",
								  "messages.yml")
							 .replace("<prefix>", prefix)));
						break;
					case "menu":
						this.liveManager.openLiveManager(player);
						player.sendMessage(TextUtils.parse(this.configurationHandler
							 .text(File.CUSTOM,
									"messages.live-menu-open",
									"messages.yml")
							 .replace("<prefix>", prefix)));
						break;
				}
			} else {
				player.playSound(player.getLocation(),
					 XSound.valueOf(this.configurationHandler.text(File.CONFIG,
								 "config.sounds.no-perm",
								 null))
							.parseSound(),
					 this.configurationHandler.number(File.CONFIG,
						  "config.sounds.volume-level",
						  null),
					 this.configurationHandler.number(File.CONFIG,
							"config.sounds.volume-level",
							null));
				player.sendMessage(TextUtils.parse(this.configurationHandler
					 .text(File.CUSTOM,
							"messages.no-perm",
							"messages.yml")
					 .replace("<prefix>", prefix)));
			}
			return false;
		}
		return false;
	}
}
