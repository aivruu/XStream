package net.xstream.bungee.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.xstream.bungee.XStream;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for strings/texts.
 *
 * @author InitSync
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Utils {
	private static final Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}}");
	private static final StringBuilder BUILDER = new StringBuilder();
	
	private Utils() {}
	
	/**
	 * Colorize the string passed as parameter, if the server version number is minor than 16 (-1.16),
	 * will colorize the colors normally, overwise, will apply the HEX colors.
	 * <p>
	 * [!] Code taken out from SternalBoard repository:
	 * github.com/ShieldCommunity/SternalBoard/blob/main/plugin/src/main/java/com/xism4
	 * /sternalboard/utils/PlaceholderUtils.java
	 *
	 * @param text Text to colorize.
	 * @return The colorized text.
	 */
	public static @NotNull String parse(@NotNull String text) {
		text = text.replace("<br>", "\n");
		
		String[] parts = text.split(String.format("((?<=%1$s)|(?=%1$s))", "&"));
		Matcher matcher = HEX_PATTERN.matcher(text);
		
		if (text.contains("&#")) {
			final int length = parts.length;
			for (int i = 0 ; i < length ; i++) {
				if (parts[i].equalsIgnoreCase("&")) {
					i++;
					if (parts[i].charAt(0) == '#') BUILDER.append(ChatColor.of(parts[i].substring(0, 7)));
					else BUILDER.append(ChatColor.translateAlternateColorCodes('&', "&" + parts[i]));
				} else {
					while (matcher.find()) {
						final String color = parts[i].substring(matcher.start(), matcher.end());
						parts[i] = parts[i].replace(color, ChatColor.of(color) + "");
						matcher = HEX_PATTERN.matcher(text);
					}
					BUILDER.append(parts[i]);
				}
			}
		} else {
			while (matcher.find()) {
				final String color = text.substring(matcher.start(), matcher.end());
				text = text.replace(color, ChatColor.of(color) + "");
				matcher = HEX_PATTERN.matcher(text);
			}
			BUILDER.append(text);
		}
		
		return BUILDER.toString();
	}
	
	/**
	 * Sends a title to connected player.
	 *
	 * @param player ProxiedPlayer object.
	 * @param title Title message.
	 * @param subtitle Subtitle message.
	 * @param fadeIn In coming time.
	 * @param stay Staying time.
	 * @param fadeOut Outing time.
	 */
	public static void showTitle(
		 @NotNull ProxiedPlayer player,
		 @NotNull String title,
		 @NotNull String subtitle,
		 int fadeIn,
		 int stay,
		 int fadeOut
	) {
		Objects.requireNonNull(player, "The player is null.");
		Validate.notEmpty(title, "The title message is empty.");
		Validate.notEmpty(subtitle, "The subtitle message is empty.");
		
		XStream.instance()
			 .getProxy()
			 .createTitle()
			 .title(TextComponent.fromLegacyText(parse(title)))
			 .subTitle(TextComponent.fromLegacyText(parse(subtitle)))
			 .fadeIn(fadeIn)
			 .stay(stay)
			 .fadeOut(fadeOut)
			 .send(player);
	}
}

