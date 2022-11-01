package net.xstream.bungee.services;

import net.xconfig.bungee.config.BungeeConfigurationHandler;
import net.xstream.bungee.XStream;
import net.xstream.bungee.managers.LiveManagerImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface ManagerService {
	/**
	 * Returns a new LiveManagerImpl object.
	 *
	 * @param configurationHandler A BukkitConfigurationHandler object.
	 * @param plugin A XStream object.
	 * @return A LiveManagerImpl instance.
	 */
	@Contract ("_, _ -> new")
	static @NotNull LiveManagerImpl bungeeLiveManager(
		 @NotNull XStream plugin,
		 @NotNull BungeeConfigurationHandler configurationHandler
	) {
		return new LiveManagerImpl(plugin, configurationHandler);
	}
}
