package net.xstream.plugin.builders;

import com.cryptomorin.xseries.XMaterial;
import net.xstream.plugin.utils.TextUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public final class ItemBuilder {
	private ItemBuilder() {}
	
	public static class Builder {
		private final String materialName;
		
		private int amount;
		private String displayName;
		private String lore;
		
		public Builder(@NotNull String materialName) {
			Validate.notEmpty(materialName, "The material name is empty.");
			this.materialName = materialName;
		}
		
		public Builder amount(int amount) {
			this.amount = amount;
			return this;
		}
		
		public Builder displayName(@NotNull String displayName) {
			Validate.notEmpty(displayName, "The display name is empty.");
			this.displayName = TextUtils.parse(displayName);
			return this;
		}
		
		public Builder lore(@NotNull String lore) {
			this.lore = TextUtils.parse(lore);
			return this;
		}
		
		public ItemStack build() {
			final ItemStack itemStack = new ItemStack(XMaterial.matchXMaterial(this.materialName)
				 .get()
				 .parseMaterial(), this.amount);
			final ItemMeta meta = itemStack.getItemMeta();
			assert meta != null;
			meta.setDisplayName(this.displayName);
			meta.setLore(Arrays.asList(this.lore.split("\n")));
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,
				 ItemFlag.HIDE_DESTROYS,
				 ItemFlag.HIDE_ENCHANTS,
				 ItemFlag.HIDE_PLACED_ON,
				 ItemFlag.HIDE_POTION_EFFECTS,
				 ItemFlag.HIDE_UNBREAKABLE);
			
			itemStack.setItemMeta(meta);
			return itemStack;
		}
	}
}
