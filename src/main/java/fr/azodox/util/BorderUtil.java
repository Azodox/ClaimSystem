package fr.azodox.util;

import net.minecraft.network.protocol.game.ClientboundInitializeBorderPacket;
import net.minecraft.world.level.border.WorldBorder;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public final class BorderUtil {

    private BorderUtil(){}

    public static void resetBorder(Player player){
        var world = Bukkit.getWorld("world");
        var worldBorder = world.getWorldBorder();
        var nmsWorldBorder = new WorldBorder();

        nmsWorldBorder.world = ((CraftWorld) world).getHandle();
        nmsWorldBorder.c(worldBorder.getCenter().getX(), worldBorder.getCenter().getZ());
        nmsWorldBorder.a(worldBorder.getSize());
        nmsWorldBorder.c(worldBorder.getDamageAmount());
        nmsWorldBorder.b(worldBorder.getDamageBuffer());
        nmsWorldBorder.c(worldBorder.getWarningDistance());
        nmsWorldBorder.b(worldBorder.getWarningTime());

        ((CraftPlayer) player).getHandle().b.a(new ClientboundInitializeBorderPacket(nmsWorldBorder));
    }
}
