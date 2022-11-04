package net.xstream.plugin.builders;

import net.xstream.plugin.XStream;
import net.xstream.plugin.services.BuilderService;
import net.xstream.plugin.utils.TextUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class InventoryBuilder {
	private InventoryBuilder() {}
	
	public static class Builder {
		private final XStream plugin;
		
		private String title;
		private int size;
		private ConfigurationSection section;
		
		public Builder(@NotNull XStream plugin) {
			this.plugin = Objects.requireNonNull(plugin, "The XStream instance is null.");
		}
		
		public Builder title(@NotNull String title) {
			Validate.notEmpty(title, "The inventory title is empty.");
			this.title = TextUtils.parse(title);
			return this;
		}
		
		public Builder size(int size) {
			this.size = size;
			return this;
		}
		
		public Builder configSection(@NotNull ConfigurationSection section) {
			this.section = Objects.requireNonNull(section, "The section is null.");
			return this;
		}
		
		public Inventory build() {
			final Inventory inventory = Bukkit.createInventory(null, this.size, this.title);
			for (String key : this.section.getKeys(false)) {
				inventory.setItem(this.section.getInt(key + ".slot"),
					 BuilderService.fromMaterial(this.section.getString(key + ".material"))
						  .amount(1)
						  .displayName(this.section.getString(key + ".display-name"))
						  .lore(this.section.getString(key + ".lore"))
						  .build());
			}
			
			return inventory;
		}
	}
}
