package net.xstream.bukkit.services;

import com.cryptomorin.xseries.XMaterial;
import net.xstream.bukkit.builders.ItemBuilder;
import org.jetbrains.annotations.NotNull;

public interface BuilderService {
	static ItemBuilder.Builder fromMaterial(@NotNull XMaterial material) {
		return new ItemBuilder.Builder(material.parseMaterial());
	}
}
