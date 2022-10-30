package net.xstream;

import net.md_5.bungee.api.plugin.Plugin;
import net.xstream.services.LoaderService;
import org.jetbrains.annotations.NotNull;

public final class XStream extends Plugin {
	private static XStream instance;
	
	private Loader loader;
	
	public XStream() {
		instance = this;
		
		this.loader = LoaderService.loader(this);
	}
	
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
		
		if (instance != null) instance = null;
	}
}
