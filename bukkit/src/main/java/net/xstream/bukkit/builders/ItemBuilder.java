package net.xstream.bukkit.builders;

import net.xstream.bukkit.utils.TextUtils;
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
		private final Material material;
		
		private int amount;
		private String displayName;
		private String lore;
		
		public Builder(@NotNull Material material) {
			this.material = Objects.requireNonNull(material, "The material is null.");
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
			final ItemStack itemStack = new ItemStack(this.material, this.amount);
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
