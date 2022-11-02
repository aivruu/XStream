package net.xstream.bukkit.managers;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import net.xconfig.bukkit.config.BukkitConfigurationHandler;
import net.xconfig.enums.File;
import net.xstream.api.managers.BukkitLiveManager;
import net.xstream.api.spigot.events.StreamAnnounceEvent;
import net.xstream.bukkit.XStream;
import net.xstream.bukkit.services.BuilderService;
import net.xstream.bukkit.utils.TextUtils;
import net.xstream.bukkit.utils.Utils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import team.unnamed.gui.menu.item.ItemClickable;
import team.unnamed.gui.menu.type.MenuInventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class LiveManagerImpl implements BukkitLiveManager {
	private final Map<UUID, String> streams;
	private final Map<UUID, BukkitTask> tasks;
	private final BukkitConfigurationHandler configurationHandler;
	private final ItemStack announceItem;
	private final ItemStack usageItem;
	private final ItemStack closeItem;
	
	public LiveManagerImpl(@NotNull BukkitConfigurationHandler configurationHandler) {
		this.streams = new HashMap<>();
		this.tasks = new HashMap<>();
		this.configurationHandler = Objects.requireNonNull(configurationHandler,
			 "The BukkitConfigurationHandler is null.");
		this.announceItem = BuilderService.fromMaterial(XMaterial.valueOf(
					this.configurationHandler.text(File.CONFIG,
						 "config.live.announce-live.material",
						 null)))
			 .amount(1)
			 .displayName(this.configurationHandler.text(File.CONFIG, "config.live.announce-live.display-name", null))
			 .lore(this.configurationHandler.text(File.CONFIG, "config.live.announce-live.lore",
				  null))
			 .build();
		this.usageItem = BuilderService.fromMaterial(XMaterial.valueOf(
					this.configurationHandler.text(File.CONFIG,
						 "config.live.usage.material",
						 null)))
			 .amount(1)
			 .displayName(this.configurationHandler.text(File.CONFIG, "config.live.usage.display-name", null))
			 .lore(this.configurationHandler.text(File.CONFIG, "config.live.usage.lore", null))
			 .build();
		this.closeItem = BuilderService.fromMaterial(XMaterial.valueOf(
					this.configurationHandler.text(File.CONFIG,
						 "config.live.close-menu.material",
						 null)))
			 .amount(1)
			 .displayName(this.configurationHandler.text(File.CONFIG, "config.live.close-menu.display-name", null))
			 .lore(this.configurationHandler.text(File.CONFIG, "config.live.close-menu.lore", null))
			 .build();
	}
	
	@Override
	public boolean isStreaming(@NotNull UUID uuid) {
		Objects.requireNonNull(uuid, "The uuid is null.");
		
		return this.streams.containsKey(uuid) && this.tasks.containsKey(uuid);
	}
	
	/**
	 * Sets the link of stream.
	 *
	 * @param uuid UUID of streamer.
	 * @param link Stream link.
	 */
	@Override
	public void prepare(@NotNull UUID uuid, @NotNull String link) {
		Objects.requireNonNull(uuid, "The uuid is null.");
		Validate.notEmpty(link, "The link is empty.");
		
		this.streams.put(uuid, link);
	}
	
	/**
	 * Start a task to announce the stream.
	 *
	 * @param uuid
	 */
	@Override
	public void announce(@NotNull UUID uuid) {
		Objects.requireNonNull(uuid, "The uuid is null.");
		
		final BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(XStream.instance(), () -> {
			Bukkit.getOnlinePlayers().forEach(player -> {
				player.sendMessage(TextUtils.parse(this.configurationHandler
					 .text(File.CUSTOM,
							"messages.announce-message",
							"messages.yml")
					 .replace("<player_name>", Bukkit.getPlayer(uuid).getName())
					 .replace("<stream_url>", this.streams.get(uuid))));
			});
		}, 1L, this.configurationHandler.number(File.CONFIG,
			 "config.announces-delay",
			 null));
		
		this.tasks.put(uuid, task);
	}
	
	/**
	 * Sets as offline the stream.
	 *
	 * @param uuid UUID of streamer.
	 */
	@Override
	public void offline(@NotNull UUID uuid) {
		Objects.requireNonNull(uuid, "The uuid is null.");
		
		this.streams.remove(uuid);
		this.tasks
			 .remove(uuid)
			 .cancel();
	}
	
	/**
	 * Opens the Live Manager Menu to player.
	 *
	 * @param player Player to open the menu.
	 */
	@Override
	public void openLiveManager(@NotNull Player player) {
		Objects.requireNonNull(player, "The player is null.");
		
		final UUID playerId = player.getUniqueId();
		
		player.openInventory(MenuInventory.newBuilder(TextUtils.parse(
			 this.configurationHandler.text(File.CONFIG,
				  "config.live.menu-title",
				  null)),
			 this.configurationHandler.number(File.CONFIG,
				  "config.live.rows",
				  null))
			 .item(ItemClickable.builder(this.configurationHandler.number(File.CONFIG,
				  "config.live.announce-live.slot",
				  null))
				  .item(this.announceItem)
				  .action(inventory -> {
					  if (!this.streams.containsKey(playerId)) {
						  player.sendMessage(TextUtils.parse(this.configurationHandler
							   .text(File.CUSTOM,
								    "messages.live-url-null",
								    "messages.yml")
							   .replace("<prefix>", this.configurationHandler.text(File.CONFIG, "config.prefix", null))));
							return true;
					  }
						
						if (this.tasks.containsKey(playerId)) {
							player.sendMessage(TextUtils.parse(this.configurationHandler
								 .text(File.CUSTOM,
									  "messages.live-already-announced",
									  "messages.yml")
								 .replace("<prefix>", this.configurationHandler.text(File.CONFIG, "config.prefix", null))));
							return true;
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
							
							this.announce(playerId);
						}
						return true;
				  })
				  .build())
			 .item(ItemClickable.builder(this.configurationHandler.number(File.CONFIG,
				  "config.live.usage.slot",
				  null))
				  .item(this.usageItem)
				  .build())
			 .item(ItemClickable.builder(this.configurationHandler.number(File.CONFIG,
				  "config.live.close-menu.slot",
				  null))
				  .item(this.closeItem)
				  .action(inventory -> {
						player.closeInventory();
						return false;
				  })
				  .build())
			 .build());
	}
}
