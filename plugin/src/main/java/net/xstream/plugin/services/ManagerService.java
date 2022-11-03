package net.xstream.plugin.services;

import net.xconfig.bukkit.config.BukkitConfigurationHandler;
import net.xstream.plugin.managers.LiveManagerImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface ManagerService {
	/**
	 * Returns a new LiveManagerImpl object.
	 *
	 * @param configurationHandler A BukkitConfigurationHandler object.
	 * @return A LiveManagerImpl instance.
	 */
	@Contract ("_ -> new")
	static @NotNull LiveManagerImpl liveManager(@NotNull BukkitConfigurationHandler configurationHandler) {
		return new LiveManagerImpl(configurationHandler);
	}
}
