package fr.azodox.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ItemBuilder {

    private final ItemStack it;

    public ItemBuilder(Material material){
        it = new ItemStack(material);
    }

    public ItemBuilder(ItemStack it) {
        this.it = it;
    }

    public ItemBuilder setAmount(int amount) {
        this.it.setAmount(amount);
        return this;
    }

    public ItemBuilder setData(int data) {
        ItemMeta im = it.getItemMeta();
        im.setCustomModelData(data);
        it.setItemMeta(im);
        return this;
    }

    public ItemBuilder setName(String name) {
        ItemMeta im = it.getItemMeta();
        im.setDisplayName(name);
        it.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta im = it.getItemMeta();
        im.setLore(lore);
        it.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLore(String...lore) {
        ItemMeta im = it.getItemMeta();
        im.setLore(Arrays.asList(lore));
        it.setItemMeta(im);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        ItemMeta im = it.getItemMeta();
        im.setUnbreakable(unbreakable);
        it.setItemMeta(im);
        return this;
    }

    public ItemBuilder setEnchantments(HashMap<Enchantment, Integer> enchantments) {
        this.it.addEnchantments(enchantments);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level){
        this.it.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder withItemFlags(ItemFlag... flags) {
        ItemMeta im = it.getItemMeta();
        im.addItemFlags(flags);
        it.setItemMeta(im);
        return this;
    }

    public ItemStack build() {
        return it;
    }
}