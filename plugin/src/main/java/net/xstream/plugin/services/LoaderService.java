package net.xstream.plugin.services;

import net.xstream.plugin.Loader;
import net.xstream.plugin.XStream;
import net.xstream.plugin.loaders.CommandLoader;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Interface to handle loaders instances.
 *
 * @author InitSync
 * @version 1.0.0
 * @since 1.0.0
 */
public interface LoaderService {
	/**
	 * Returns a new instance of Loader object.
	 *
	 * @param plugin The XStream instance.
	 * @return A Loader object.
	 */
	@Contract ("_ -> new")
	static @NotNull Loader loader(@NotNull XStream plugin) {
		return new Loader(plugin);
	}
	
	/**
	 * Returns a new instance of CommandLoader.Builder object.
	 *
	 * @param plugin The XStream instance.
	 * @return A CommandLoader.Builder object.
	 */
	@Contract (value = "_ -> new", pure = true)
	static CommandLoader.@NotNull Builder commandLoader(@NotNull XStream plugin) {
		return new CommandLoader.Builder(plugin);
	}
}
