package net.xstream.plugin.utils;

import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for use with Strings.
 *
 * @author InitSync
 * @version 1.0.0
 * @since 1.0.0
 */
public final class TextUtils {
	private static final int VERSION = Integer.parseInt(Bukkit.getBukkitVersion()
		 .split("-")[0]
		 .split("\\.")[1]);
	private static final Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}}");
	private static final StringBuilder BUILDER = new StringBuilder();
	
	private TextUtils() {}
	
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
		
		if (VERSION < 16) return ChatColor.translateAlternateColorCodes('&', text);
		
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
	 * Checks if the text passed as argument is valid url.
	 *
	 * @param url The url to check.
	 * @return A boolean value
	 */
	public static boolean isValidUrl(@NotNull String url) {
		Validate.notEmpty(url, "The url is empty.");
		
		try {
			new URL(url).toURI();
			return true;
		} catch (Exception ignored) { return false; }
	}
}
