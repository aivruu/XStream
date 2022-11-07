package net.xstream.plugin.enums;

import org.jetbrains.annotations.NotNull;

/**
 * Enum for handle plugin permissions.
 *
 * @author InitSync
 * @version 1.0.2
 * @since 1.0.0
 */
public enum Permission {
	HELP_CMD ("xstream.help"),
	RELOAD_CMD ("xstream.reload"),
	LIVE_CMD ("xstream.live");
	
	private final String perm;
	
	Permission(@NotNull String perm) {
		this.perm = perm;
	}
	
	public @NotNull String getPerm() {
		return this.perm;
	}
}
