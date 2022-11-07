package net.xstream.plugin.utils;

import com.cryptomorin.xseries.messages.Titles;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Utility class to handle visual messages for the player.
 *
 * @author InitSync
 * @version 1.0.2
 * @since 1.0.0
 */
public final class Utils {
	/**
	 * Sends a title to player.
	 *
	 * @param player Player object.
	 * @param title Title message.
	 * @param subtitle Subtitle message.
	 * @param fadeIn In coming time.
	 * @param stay Staying time.
	 * @param fadeOut Outing time.
	 */
	public static void showTitle(
		 @NotNull Player player,
		 @NotNull String title,
		 @NotNull String subtitle,
		 int fadeIn,
		 int stay,
		 int fadeOut
	) {
		Objects.requireNonNull(player, "The player is null.");
		Validate.notEmpty(title, "The title message is empty.");
		Validate.notEmpty(subtitle, "The subtitle message is empty.");
		
		Titles.sendTitle(player,
			 fadeIn,
			 stay,
			 fadeOut,
			 TextUtils.parse(player, title),
			 TextUtils.parse(player, subtitle));
	}
}
