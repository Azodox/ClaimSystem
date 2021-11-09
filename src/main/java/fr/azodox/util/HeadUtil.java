package fr.azodox.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.codec.binary.Base64;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class HeadUtil {

    private static final Map<String, String> translator = new HashMap<>();
    private static final Map<String, ItemStack> heads = new HashMap<>();

    /**
     * Get a head from the heads map
     * @param name : The shortcut name for heads
     * @return a head
     */
    public static ItemStack getHead(String name){
        return heads.get(translator.get(name));
    }

    /**
     * Add a head in the heads HashMap
     * @param id : The head's id
     * @param name : The head's name (shortcut name to simplify getting a head, for example: "+" instead of "5+48s5145841.12")
     */
    public void addHead(String id, String name){
        heads.put(id, getSkull(getTextureURLByMcId(id)));
        translator.put(name, id);
    }

    /**
     * Remove a head from the heads map
     * @param id : The head's id
     * @param name : The head's name (shortcut name)
     * @see #addHead
     */
    public void removeHead(String id, String name) {
        heads.remove(id);
        translator.remove(name);
    }

    /**
     * Get the head as ItemStack (player head) from an url
     * @param url
     * @return
     */
    private ItemStack getSkull(String url) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        if(url.isEmpty()) return head;


        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }

    private String getTextureURLByMcId(String id){
        return "http://textures.minecraft.net/texture/" + id;
    }
}
