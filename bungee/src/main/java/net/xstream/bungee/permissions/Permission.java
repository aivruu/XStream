package net.xstream.bungee.permissions;

import org.jetbrains.annotations.NotNull;

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
