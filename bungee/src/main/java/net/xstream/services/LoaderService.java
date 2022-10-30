package net.xstream.services;

import net.xstream.Loader;
import net.xstream.XStream;
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
}
