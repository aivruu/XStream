package net.xstream.plugin.utils;

import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.messages.Titles;
import net.xstream.plugin.XStream;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Utility class to handle visual messages for the player.
 *
 * @author InitSync
 * @version 1.0.0
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
			 TextUtils.parse(title),
			 TextUtils.parse(subtitle));
	}
	
	/**
	 * Sends an actionbar to player with a duration.
	 *
	 * @param player Player object.
	 * @param message Message to send.
	 * @param duration Duration in ticks.
	 */
	public static void showActionBar(@NotNull Player player, @NotNull String message, long duration) {
		Objects.requireNonNull(player, "The player is null.");
		Validate.notEmpty(message, "The actionbar message is empty.");
		
		ActionBar.sendActionBar(XStream.instance(),
			 player,
			 message,
			 duration);
	}
}
