package fr.azodox.util;

import net.minecraft.network.protocol.game.ClientboundInitializeBorderPacket;
import net.minecraft.world.level.border.WorldBorder;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public final class BorderUtil {

    private BorderUtil(){}

    public static void resetBorder(Player player){
        var world = Bukkit.getWorld("world");
        var worldBorder = world.getWorldBorder();
        var nmsWorldBorder = new WorldBorder();

        nmsWorldBorder.world = ((CraftWorld) world).getHandle();
        nmsWorldBorder.setCenter(worldBorder.getCenter().getX(), worldBorder.getCenter().getZ());
        nmsWorldBorder.setSize(worldBorder.getSize());
        nmsWorldBorder.setDamageAmount(worldBorder.getDamageAmount());
        nmsWorldBorder.setDamageBuffer(worldBorder.getDamageBuffer());
        nmsWorldBorder.setWarningDistance(worldBorder.getWarningDistance());
        nmsWorldBorder.setWarningTime(worldBorder.getWarningTime());

        ((CraftPlayer) player).getHandle().b.sendPacket(new ClientboundInitializeBorderPacket(nmsWorldBorder));
    }
}
