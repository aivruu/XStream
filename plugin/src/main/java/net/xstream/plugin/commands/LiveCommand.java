package net.xstream.plugin.commands;

import com.cryptomorin.xseries.XSound;
import net.xconfig.bukkit.config.BukkitConfigurationHandler;
import net.xstream.api.events.StreamAnnounceEvent;
import net.xstream.api.managers.LiveManager;
import net.xstream.api.events.OfflineStreamEvent;
import net.xstream.api.events.StreamPrepareEvent;
import net.xstream.plugin.XStream;
import net.xstream.plugin.enums.Permission;
import net.xstream.plugin.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public final class LiveCommand implements CommandExecutor {
	private final PluginManager pluginManager;
	private final BukkitConfigurationHandler configurationHandler;
	private final LiveManager liveManager;
	
	public LiveCommand(
		 @NotNull BukkitConfigurationHandler configurationHandler,
		 @NotNull LiveManager liveManager
	) {
		this.pluginManager = XStream.instance()
			 .getServer()
			 .getPluginManager();
		this.configurationHandler = Objects.requireNonNull(configurationHandler,
			 "The BukkitConfigurationHandler object is null.");
		this.liveManager = Objects.requireNonNull(liveManager, "The BukkitLiveManager object is null.");
	}
	
	@Override
	public boolean onCommand(
		 @NotNull CommandSender sender,
		 @NotNull Command command,
		 @NotNull String label,
		 @NotNull String[] args
	) {
		final String prefix = this.configurationHandler.text("config.yml", "config.prefix");
		
		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage(TextUtils.colorize(this.configurationHandler
				 .text("messages.yml", "messages.no-console")
				 .replace("<prefix>", prefix)));
			return false;
		}
		
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			final UUID playerId = player.getUniqueId();
			
			if (player.hasPermission(Permission.LIVE_CMD.getPerm())) {
				if (args.length == 0) {
					player.sendMessage(TextUtils.colorize(this.configurationHandler
						 .text("messages.yml", "messages.live-usage")
						 .replace("<prefix>", prefix)));
					return false;
				}
				
				switch (args[0]) {
					default:
						player.sendMessage(TextUtils.colorize(this.configurationHandler
							 .text("messages.yml", "messages.no-command")
							 .replace("<prefix>", prefix)));
						break;
					case "offline":
						if (!this.liveManager.isStreaming(playerId)) {
							player.sendMessage(TextUtils.colorize(this.configurationHandler
								 .text("messages.yml", "messages.live-not-started")
								 .replace("<prefix>", prefix)));
							break;
						}
						
						final OfflineStreamEvent offlineStreamEvent = new OfflineStreamEvent(player);
						this.pluginManager.callEvent(offlineStreamEvent);
						if (!offlineStreamEvent.isCancelled()) {
							this.liveManager.offline(playerId);
							player.sendMessage(TextUtils.colorize(this.configurationHandler
								 .text("messages.yml", "messages.live-offline")
								 .replace("<prefix>", prefix)));
							Bukkit.getOnlinePlayers().forEach(connected -> {
								connected.sendMessage(TextUtils.parse(connected, this.configurationHandler
									 .text("messages.yml", "messages.announce-offline")
									 .replace("<player_name>", player.getName())));
							});
						}
						break;
					case "url":
						if (args.length == 1) {
							player.sendMessage(TextUtils.colorize(this.configurationHandler
								 .text("messages.yml", "messages.live-missing-link")
								 .replace("<prefix>", prefix)));
							return false;
						}
						
						if (!TextUtils.isValidUrl(args[1])) {
							player.sendMessage(TextUtils.colorize(this.configurationHandler
								 .text("messages.yml", "messages.live-format")
								 .replace("<prefix>", prefix)));
							return false;
						}
						
						final StreamPrepareEvent streamPrepareEvent = new StreamPrepareEvent(args[1]);
						this.pluginManager.callEvent(streamPrepareEvent);
						if (!streamPrepareEvent.isCancelled()) {
							this.liveManager.prepare(playerId, args[1]);
							player.sendMessage(TextUtils.colorize(this.configurationHandler
								 .text("messages.yml", "messages.live-url-set")
								 .replace("<prefix>", prefix)));
						}
						break;
					case "send":
						final StreamAnnounceEvent streamAnnounceEvent = new StreamAnnounceEvent();
						this.pluginManager.callEvent(streamAnnounceEvent);
						if (!streamAnnounceEvent.isCancelled()) {
							if (!this.liveManager.streams().containsKey(playerId)) {
								player.sendMessage(TextUtils.colorize(this.configurationHandler
									 .text("messages.yml", "messages.live-url-null")
									 .replace("<prefix>", prefix)));
								return false;
							}
							
							if (this.liveManager.tasks().containsKey(playerId)) {
								player.sendMessage(TextUtils.colorize(this.configurationHandler
									 .text("messages.yml", "messages.live-already-announced")
									 .replace("<prefix>", prefix)));
								return false;
							}
							
							this.liveManager.announce(playerId);
							player.sendMessage(TextUtils.parse(player, this.configurationHandler
								 .text("messages.yml", "messages.live-announce")
								 .replace("<prefix>", prefix)));
						}
						break;
				}
			} else {
				player.playSound(player.getLocation(),
					XSound.valueOf(this.configurationHandler.text("config.yml", "config.sounds.no-perm")).parseSound(),
					this.configurationHandler.number("config.yml", "config.sounds.volume-level"),
					this.configurationHandler.number("config.yml", "config.sounds.volume-level"));
				player.sendMessage(TextUtils.colorize(this.configurationHandler
					 .text("messages.yml", "messages.no-perm")
					 .replace("<prefix>", prefix)));
			}
			return false;
		}
		return false;
	}
}
