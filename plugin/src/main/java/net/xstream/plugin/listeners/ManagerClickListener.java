package net.xstream.plugin.listeners;

import com.cryptomorin.xseries.XSound;
import net.xconfig.bukkit.config.BukkitConfigurationHandler;
import net.xconfig.enums.File;
import net.xstream.api.events.StreamAnnounceEvent;
import net.xstream.api.managers.LiveManager;
import net.xstream.plugin.XStream;
import net.xstream.plugin.utils.TextUtils;
import net.xstream.plugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public final class ManagerClickListener implements Listener {
	private final LiveManager liveManager;
	private final BukkitConfigurationHandler configurationHandler;
	private final ConfigurationSection section;
	
	public ManagerClickListener(
		 @NotNull LiveManager liveManager,
		 @NotNull BukkitConfigurationHandler configurationHandler
	) {
		this.liveManager = Objects.requireNonNull(liveManager, "The LiveManager object is null.");
		this.configurationHandler = Objects.requireNonNull(configurationHandler,
			 "The BukkitConfigurationHandler object is null.");
		this.section = XStream.instance()
			 .loader()
			 .configurationHandler()
			 .configSection(File.CONFIG,
				  "config.live.content",
				  null);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		final Player player = (Player) event.getWhoClicked();
		final UUID playerId = event.getWhoClicked().getUniqueId();
		final int slot = event.getSlot();
		
		for (String key : this.section.getKeys(false)) {
			if (this.section.contains(key + ".slot") && slot == this.section.getInt(key + ".slot")) {
				event.setCancelled(true);
				return;
			}
		}
		
		if (slot == this.configurationHandler.number(File.CONFIG,
			 "config.live.content.announce-live.slot",
			 null)
		) {
			event.setCancelled(true);
			if (!this.liveManager.streams().containsKey(playerId)) {
				player.sendMessage(TextUtils.parse(this.configurationHandler
					 .text(File.CUSTOM,
							"messages.live-url-null",
							"messages.yml")
					 .replace("<prefix>", this.configurationHandler.text(File.CONFIG, "config.prefix", null))));
				return;
			}
			
			if (this.liveManager.tasks().containsKey(playerId)) {
				player.sendMessage(TextUtils.parse(this.configurationHandler
					 .text(File.CUSTOM,
							"messages.live-already-announced",
							"messages.yml")
					 .replace("<prefix>", this.configurationHandler.text(File.CONFIG, "config.prefix", null))));
				return;
			}
			
			final StreamAnnounceEvent streamAnnounceEvent = new StreamAnnounceEvent();
			Bukkit.getPluginManager().callEvent(streamAnnounceEvent);
			if (!streamAnnounceEvent.isCancelled()) {
				if (this.configurationHandler.condition(File.CONFIG,
					 "config.titles.allow",
					 null)
				) {
					Bukkit.getOnlinePlayers().forEach(connected -> {
						connected.playSound(connected.getLocation(),
							 XSound.valueOf(this.configurationHandler.text(File.CONFIG,
									"config.sounds.live",
									null)).parseSound(),
							 this.configurationHandler.number(File.CONFIG,
									"config.sounds.volume-level",
									null),
							 this.configurationHandler.number(File.CONFIG,
									"config.sounds.volume-level",
									null));
						Utils.showTitle(connected,
							 this.configurationHandler
									.text(File.CUSTOM,
										 "messages.announce-title",
										 "messages.yml")
									.replace("<player_name>", player.getName()),
							 this.configurationHandler
									.text(File.CUSTOM,
										 "messages.announce-subtitle",
										 "messages.yml")
									.replace("<player_name>", player.getName()),
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
			return;
		}
		
		if (slot == this.configurationHandler.number(File.CONFIG,
			 "config.live.content.usage.slot",
			 null)) {
			event.setCancelled(true);
			return;
		}
		
		if (slot == this.configurationHandler.number(File.CONFIG,
			 "config.live.content.offline.slot",
			 null)
		) {
			event.setCancelled(true);
			player.chat("/live offline");
			return;
		}
		
		if (slot == this.configurationHandler.number(File.CONFIG,
			 "config.live.content.offline-close.slot",
			 null)
		) {
			event.setCancelled(true);
			player.closeInventory();
		}
	}
}
