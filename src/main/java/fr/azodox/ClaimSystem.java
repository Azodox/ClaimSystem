package fr.azodox;

import fr.azodox.blocks.CBlockLI;
import fr.azodox.blocks.ICBlock;
import fr.azodox.events.*;
import fr.azodox.inventory.util.FastInvManager;
import fr.azodox.task.ParticleTask;
import fr.azodox.util.Cuboid;
import fr.azodox.util.HeadUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ClaimSystem extends JavaPlugin {

    public static final int MAX_CLAIMS = 1;
    public static final String PLUGIN_PREFIX = ChatColor.GOLD + "§lClaimSystem §8§l➤ " + ChatColor.YELLOW + "";
    private final List<ICBlock> registeredCBlocks = new ArrayList<>();
    private final Map<UUID, Cuboid> fakesParticles = new HashMap<>();
    private HeadUtil headUtil;

    @Override
    public void onEnable() {
        headUtil = new HeadUtil();
        FastInvManager.register(this);

        registerTasks();
        registerBlocks();
        registerHeads();

        registerEvents(
                new PlayerJoinListener(this),
                new PlayerHoldItemListener(this),
                new PlayerMoveListener(this),
                new BlockPlaceListener(this),
                new RegionEnteredListener(),
                new RegionLeftListener(),
                new BlockBreakListener(),
                new BlockExplodeListener(),
                new PlayerInteractListener());
        getLogger().info("Enabled!");
    }

    private void registerHeads() {
        headUtil.addHead(/*+ Head*/"9a2d891c6ae9f6baa040d736ab84d48344bb6b70d7f1a280dd12cbac4d777", "plus");
        headUtil.addHead("e4d49bae95c790c3b1ff5b2f01052a714d6185481d5b1c85930b3f99d2321674", "wrench");
        headUtil.addHead("935e4e26eafc11b52c11668e1d6634e7d1d0d21c411cb085f9394268eb4cdfba", "minus");
        headUtil.addHead("a497650457c69cffcbcf680eeb6a231a6abeb00cca5eadd7c1c68900d782e3b9", "permission");
        headUtil.addHead("864f779a8e3ffa231143fa69b96b14ee35c16d669e19c75fd1a7da4bf306c", "back");
    }

    private void registerBlocks() {
        registeredCBlocks.add(new CBlockLI());
    }

    private void registerTasks() {
        new ParticleTask(this).runTaskTimer(this, 0, 20);
    }

    private void registerEvents(Listener...listeners){
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    public Map<UUID, Cuboid> getFakesParticles() {
        return fakesParticles;
    }

    public List<ICBlock> getRegisteredCBlocks() {
        return registeredCBlocks;
    }

    @NotNull
    public ICBlock getClaimBlock(int level){
        return registeredCBlocks.stream().filter(c -> c.getLevel(false).equals(String.valueOf(level))).findFirst().get();
    }
}
