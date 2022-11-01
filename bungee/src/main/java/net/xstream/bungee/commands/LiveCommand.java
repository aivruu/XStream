package net.xstream.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.PluginManager;
import net.xconfig.bungee.config.BungeeConfigurationHandler;
import net.xconfig.enums.File;
import net.xstream.api.bungee.events.OfflineStreamEvent;
import net.xstream.api.bungee.events.StreamAnnounceEvent;
import net.xstream.api.bungee.events.StreamPrepareEvent;
import net.xstream.api.managers.BungeeLiveManager;
import net.xstream.bungee.XStream;
import net.xstream.bungee.enums.Permission;
import net.xstream.bungee.utils.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public final class LiveCommand extends Command {
	private final XStream plugin;
	private final PluginManager pluginManager;
	private final BungeeConfigurationHandler configurationHandler;
	private final BungeeLiveManager liveManager;
	
	public LiveCommand(
		 @NotNull XStream plugin,
		 @NotNull BungeeConfigurationHandler configurationHandler,
		 @NotNull BungeeLiveManager liveManager
	) {
		super("xstream", null, "xs");
		this.plugin = Objects.requireNonNull(plugin, "The XStream instance is null.");
		this.pluginManager = XStream.instance()
			 .getProxy()
			 .getPluginManager();
		this.configurationHandler = Objects.requireNonNull(configurationHandler,
			 "The BukkitConfigurationHandler object is null.");
		this.liveManager = Objects.requireNonNull(liveManager, "The BukkitLiveManager object is null.");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		final String prefix = this.configurationHandler.text(
			 File.CONFIG,
			 "config.prefix",
			 null);
		
		if (!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage(TextComponent.fromLegacyText(Utils.parse(this.configurationHandler
				 .text(File.CUSTOM,
						"messages.no-console",
						"messages.yml")
				 .replace("<prefix>", prefix))));
			return;
		}
		
		final ProxiedPlayer player = (ProxiedPlayer) sender;
		final UUID playerId = player.getUniqueId();
		
		if (player.hasPermission(Permission.LIVE_CMD.getPerm())) {
			if (args.length == 0) {
				player.sendMessage(TextComponent.fromLegacyText(Utils.parse(this.configurationHandler
					 .text(File.CUSTOM,
							"messages.live-usage",
							"messages.yml")
					 .replace("<prefix>", prefix))));
				return;
			}
			
			switch (args[0]) {
				default:
					player.sendMessage(TextComponent.fromLegacyText(Utils.parse(this.configurationHandler
						 .text(File.CUSTOM,
								"messages.no-command",
								"messages.yml")
						 .replace("<prefix>", prefix))));
					break;
				case "offline":
					if (!this.liveManager.isStreaming(playerId)) {
						player.sendMessage(TextComponent.fromLegacyText(Utils.parse(this.configurationHandler
							 .text(File.CUSTOM,
									"messages.live-not-started",
									"messages.yml")
							 .replace("<prefix>", prefix))));
						break;
					}
					
					final OfflineStreamEvent offlineStreamEvent = new OfflineStreamEvent(player);
					this.pluginManager.callEvent(offlineStreamEvent);
					if (!offlineStreamEvent.isCancelled()) {
						this.liveManager.offline(playerId);
						player.sendMessage(TextComponent.fromLegacyText(Utils.parse(this.configurationHandler
							 .text(File.CUSTOM,
									"messages.live-offline",
									"messages.yml")
							 .replace("<prefix>", prefix))));
						this.plugin
							 .getProxy()
							 .broadcast(TextComponent.fromLegacyText(Utils.parse(this.configurationHandler
								  .text(File.CUSTOM,
										 "messages.announce-offline",
										 "messages.yml")
								  .replace("<player_name>", prefix))));
					}
					break;
				case "url":
					if (args.length == 1) {
						player.sendMessage(TextComponent.fromLegacyText(Utils.parse(this.configurationHandler
							 .text(File.CUSTOM,
									"messages.live-missing-link",
									"messages.yml")
							 .replace("<prefix>", prefix))));
						return;
					}
					
					final StreamPrepareEvent streamPrepareEvent = new StreamPrepareEvent(args[1]);
					this.pluginManager.callEvent(streamPrepareEvent);
					if (!streamPrepareEvent.isCancelled()) {
						this.liveManager.prepare(playerId, args[1]);
						player.sendMessage(TextComponent.fromLegacyText(Utils.parse(this.configurationHandler
							 .text(File.CUSTOM,
									"messages.live-url-set",
									"messages.yml")
							 .replace("<prefix>", prefix))));
					}
					break;
				case "send":
					if (!this.liveManager.streams().containsKey(playerId)) {
						player.sendMessage(TextComponent.fromLegacyText(Utils.parse(this.configurationHandler
							 .text(File.CUSTOM,
									"messages.live-not-started",
									"messages.yml")
							 .replace("<prefix>", this.configurationHandler.text(File.CONFIG, "config.prefix", null)))));
						return;
					}
					
					if (this.liveManager.tasks().containsKey(playerId)) {
						player.sendMessage(TextComponent.fromLegacyText(Utils.parse(this.configurationHandler
							 .text(File.CUSTOM,
									"messages.live-already-announced",
									"messages.yml")
							 .replace("<prefix>", this.configurationHandler.text(File.CONFIG, "config.prefix", null)))));
						return;
					}
					
					final StreamAnnounceEvent streamAnnounceEvent = new StreamAnnounceEvent();
					this.pluginManager.callEvent(streamAnnounceEvent);
					if (!streamAnnounceEvent.isCancelled()) {
						if (this.configurationHandler.condition(File.CONFIG,
							 "config.titles.allow",
							 null)
						) {
							this.plugin
								 .getProxy()
								 .getPlayers()
								 .forEach(connected -> {
									 Utils.showTitle(connected,
											this.configurationHandler.text(File.CUSTOM,
												 "messages.announce-title",
												 "messages.yml"),
											this.configurationHandler.text(File.CUSTOM,
												 "messages.announce-subtitle",
												 "messages.yml"),
											this.configurationHandler.number(File.CONFIG,
												 "config.titles.fade-in",
												 null),
											this.configurationHandler.number(File.CONFIG,
												 "config.titles.stay",
												 null),
											this.configurationHandler.number(File.CONFIG,
												 "config.titles.fade-out",
												 null));
								 });
						}
						
						this.liveManager.announce(playerId);
					}
					break;
			}
		} else {
			player.sendMessage(TextComponent.fromLegacyText(Utils.parse(this.configurationHandler
				 .text(File.CUSTOM,
						"messages.no-perm",
						"messages.yml")
				 .replace("<prefix>", prefix))));
		}
	}
}
