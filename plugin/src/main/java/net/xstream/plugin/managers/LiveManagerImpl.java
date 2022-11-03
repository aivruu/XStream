package net.xstream.plugin.managers;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import net.xconfig.bukkit.config.BukkitConfigurationHandler;
import net.xconfig.enums.File;
import net.xstream.api.managers.LiveManager;
import net.xstream.api.events.StreamAnnounceEvent;
import net.xstream.plugin.XStream;
import net.xstream.plugin.services.BuilderService;
import net.xstream.plugin.utils.TextUtils;
import net.xstream.plugin.utils.Utils;
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

public final class LiveManagerImpl implements LiveManager {
	private final Map<UUID, String> streams;
	private final Map<UUID, BukkitTask> tasks;
	private final BukkitConfigurationHandler configurationHandler;
	
	public LiveManagerImpl(@NotNull BukkitConfigurationHandler configurationHandler) {
		this.streams = new HashMap<>();
		this.tasks = new HashMap<>();
		this.configurationHandler = Objects.requireNonNull(configurationHandler,
			 "The BukkitConfigurationHandler is null.");
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
				this.configurationHandler
					.textList(File.CUSTOM,
						  "messages.announce-message",
						  "messages.yml")
					.forEach(msg -> {
						player.sendMessage(TextUtils.parse(
							msg.replace("<player_name>", Bukkit.getPlayer(uuid).getName()
								   .replace("<stream_url>", this.streams.get(uuid)))));
					});
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
			 .item(ItemClickable.builder(10)
				  .item(BuilderService.fromMaterial(XMaterial.LIME_CONCRETE)
					   .amount(1)
					   .displayName(this.configurationHandler.text(File.CONFIG, "config.live.announce-live.display-name", null))
					   .lore(this.configurationHandler.text(File.CONFIG, "config.live.announce-live.lore", null))
					   .build())
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
							
							this.announce(playerId);
						}
						return true;
				  })
				  .build())
			 .item(ItemClickable.builder(13)
				  .item(BuilderService.fromMaterial(XMaterial.WRITABLE_BOOK)
					   .amount(1)
					   .displayName(this.configurationHandler.text(File.CONFIG, "config.live.usage.display-name", null))
					   .lore(this.configurationHandler.text(File.CONFIG, "config.live.usage.lore", null))
					   .build())
				  .action(inventory -> {
						player.closeInventory();
						return true;
				  })
				  .build())
			 .item(ItemClickable.builder(16)
				  .item(BuilderService.fromMaterial(XMaterial.RED_CONCRETE)
					   .amount(1)
					   .displayName(this.configurationHandler.text(File.CONFIG, "config.live.offline.display-name", null))
					   .lore(this.configurationHandler.text(File.CONFIG, "config.live.offline.lore", null))
					   .build())
				  .action(inventory -> {
						player.chat("/live offline");
						return true;
				  })
				  .build())
			 .item(ItemClickable.builder(31)
				  .item(BuilderService.fromMaterial(XMaterial.BARRIER)
					   .amount(1)
					   .displayName(this.configurationHandler.text(File.CONFIG, "config.live.close-menu.display-name", null))
					   .lore(this.configurationHandler.text(File.CONFIG, "config.live.close-menu.lore", null))
					   .build())
				  .action(inventory -> {
					  player.closeInventory();
					  return true;
				  })
				  .build())
			 .item(ItemClickable.builder(27)
				  .item(new ItemStack(XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial(), 1))
				  .action(inventory -> {
					  player.closeInventory();
					  return true;
				  })
				  .build())
			 .item(ItemClickable.builder(28)
					.item(new ItemStack(XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial(), 1))
				  .action(inventory -> {
					  player.closeInventory();
					  return true;
				  })
					.build())
			 .item(ItemClickable.builder(29)
					.item(new ItemStack(XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial(), 1))
				  .action(inventory -> {
					  player.closeInventory();
					  return true;
				  })
					.build())
			 .item(ItemClickable.builder(30)
					.item(new ItemStack(XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial(), 1))
				  .action(inventory -> {
					  player.closeInventory();
					  return true;
				  })
					.build())
			 .item(ItemClickable.builder(32)
					.item(new ItemStack(XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial(), 1))
				  .action(inventory -> {
					  player.closeInventory();
					  return true;
				  })
					.build())
			 .item(ItemClickable.builder(33)
					.item(new ItemStack(XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial(), 1))
				  .action(inventory -> {
					  player.closeInventory();
					  return true;
				  })
					.build())
			 .item(ItemClickable.builder(34)
					.item(new ItemStack(XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial(), 1))
				  .action(inventory -> {
					  player.closeInventory();
					  return true;
				  })
					.build())
			 .item(ItemClickable.builder(35)
					.item(new ItemStack(XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial(), 1))
				  .action(inventory -> {
					  player.closeInventory();
					  return true;
				  })
					.build())
			 .build());
	}
}