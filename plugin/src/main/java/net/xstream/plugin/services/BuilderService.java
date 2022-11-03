package net.xstream.plugin.services;

import com.cryptomorin.xseries.XMaterial;
import net.xstream.plugin.XStream;
import net.xstream.plugin.builders.InventoryBuilder;
import net.xstream.plugin.builders.ItemBuilder;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface BuilderService {
	/**
	 * Returns a new ItemBuilder.Builder object.
	 *
	 * @param materialName Name of material to match.
	 * @return A ItemBuilder.Builder instance.
	 */
	@Contract ("_ -> new")
	static ItemBuilder.@NotNull Builder fromMaterial(@NotNull String materialName) {
		Validate.notEmpty(materialName, "The material name is empty.");
		
		return new ItemBuilder.Builder(XMaterial.matchXMaterial(materialName).get().parseMaterial());
	}
	
	/**
	 * Returns a new instance of InventoryBuilder.Builder object.
	 *
	 * @param plugin The XStream instance.
	 * @return A InventoryBuilder.Builder object.
	 */
	@Contract (value = "_ -> new", pure = true)
	static InventoryBuilder.@NotNull Builder newGui(@NotNull XStream plugin) {
		return new InventoryBuilder.Builder(plugin);
	}
}
