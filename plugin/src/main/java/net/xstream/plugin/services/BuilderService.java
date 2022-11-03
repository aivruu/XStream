package net.xstream.plugin.services;

import com.cryptomorin.xseries.XMaterial;
import net.xstream.plugin.builders.ItemBuilder;
import org.jetbrains.annotations.NotNull;

public interface BuilderService {
	static ItemBuilder.Builder fromMaterial(@NotNull XMaterial material) {
		return new ItemBuilder.Builder(material.parseMaterial());
	}
}
