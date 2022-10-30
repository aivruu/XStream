package net.xstream;

import net.xstream.services.LoaderService;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Main class.
 *
 * @author InitSync
 * @version 1.0.0
 * @since 1.0.0
 */
public final class XStream extends JavaPlugin {
	private static XStream instance;
	
	private Loader loader;
	
	/**
	 * Returns the XStream instance, if it is null, will be throws an IllegalStateException.
	 *
	 * @return A XStream instance.
	 */
	public static @NotNull XStream instance() {
		if (instance == null) {
			throw new IllegalStateException("Cannot access to the XStream instance because is null.");
		}
		return instance;
	}
	
	/**
	 * If the Loader object isn't null return it, overwise throws an IllegalStateException.
	 *
	 * @return The loader.
	 */
	public @NotNull Loader loader() {
		if (this.loader == null) {
			throw new IllegalStateException("Cannot get the Loader instance.");
		}
		return this.loader;
	}
	
	public XStream() {
		instance = this;
		
		this.loader = LoaderService.loader(this);
	}
	
	@Override
	public void onEnable() {
		this.loader.enable();
	}
	
	@Override
	public void onDisable() {
		if (this.loader != null) {
			this.loader.disable();
			this.loader = null;
		}
		
		if (instance == this) instance = null;
	}
}
