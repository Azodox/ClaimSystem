package fr.azodox;

import com.hakan.borderapi.bukkit.BorderAPI;
import fr.azodox.blocks.CBlockLI;
import fr.azodox.blocks.ICBlock;
import fr.azodox.events.*;
import fr.azodox.gui.CBlockGUI;
import fr.azodox.inventory.util.FastInvManager;
import fr.azodox.task.BorderTask;
import fr.azodox.task.ParticleTask;
import fr.azodox.util.Cuboid;
import fr.azodox.util.HeadUtil;
import fr.azodox.util.WGRegionUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * This class is the main class of this plugin.
 */
public class ClaimSystem extends JavaPlugin {

    public static final int MAX_CLAIMS = 1;
    public static final String PLUGIN_PREFIX = ChatColor.GOLD + "§lClaimSystem §8§l➤ " + ChatColor.YELLOW + "";
    
    private final List<ICBlock> registeredCBlocks = new ArrayList<>();
    /**
     * Fill up by {@link WGRegionUtil#showBorders(org.bukkit.entity.Player, com.sk89q.worldguard.protection.regions.ProtectedRegion)} 
     * Values used by {@link BorderTask#run()}
    */
    private final Map<Long, Player> borders = new HashMap<>();
    private final Map<UUID, Cuboid> fakesParticles = new HashMap<>();

    private static BorderAPI borderAPI;

    private HeadUtil headUtil;

    /**
     * {@link this#borderAPI}
     * @return borderAPI's instance
     */
    public static BorderAPI getBorderAPI() {
        return borderAPI;
    }

    /**
     * This will register all the heads needed
     * @see HeadUtil#getHead(String) to get one of those heads everywhere.
     * @see <a target="_blank" href="https://minecraft-heads.com/">Site used to get heads' texture id</a>
     */
    private void registerHeads() {
        headUtil.addHead(/*+ Head*/"9a2d891c6ae9f6baa040d736ab84d48344bb6b70d7f1a280dd12cbac4d777", "plus");
        headUtil.addHead("e4d49bae95c790c3b1ff5b2f01052a714d6185481d5b1c85930b3f99d2321674", "wrench");
        headUtil.addHead("935e4e26eafc11b52c11668e1d6634e7d1d0d21c411cb085f9394268eb4cdfba", "minus");
        headUtil.addHead("a497650457c69cffcbcf680eeb6a231a6abeb00cca5eadd7c1c68900d782e3b9", "permission");
        headUtil.addHead("864f779a8e3ffa231143fa69b96b14ee35c16d669e19c75fd1a7da4bf306c", "back");
    }

   /**
    * This method register all blocks' class present in the package {@link fr.azodox.blocks}
    * Then, blocks will be accessible with {@link #getClaimBlock(int)}
    */
    private void registerBlocks() {
        registeredCBlocks.add(new CBlockLI());
    }

    /**
     * Registering tasks used to show particles
     * @see ParticleTask
     * @see BorderTask
     */
    private void registerTasks() {
        new ParticleTask(this).runTaskTimer(this, 0, 20);
        new BorderTask(this).runTaskTimer(this, 0, 20);
    }

    /**
     * This method simplifies the way to register multiple {@link Listener}
     * @param listeners : listeners that will be registered.
     */
    private void registerEvents(Listener...listeners){
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    /**
     * Get fakes particles managed by {@link CBlockGUI#show(ICBlock, boolean)}
     * @return {@link #fakesParticles}
     */
    public Map<UUID, Cuboid> getFakesParticles() {
        return fakesParticles;
    }

    /**
     * {@linkplain #registerBlocks()}
     * @return {@link #registeredCBlocks}
     */
    public List<ICBlock> getRegisteredCBlocks() {
        return registeredCBlocks;
    }

    /**
     * Get a claim block's instance from {@link #registeredCBlocks}
     * @param level : Specified claim block's level wanted
     * @return claim block's instance
     */
    @NotNull
    public ICBlock getClaimBlock(int level){
        return registeredCBlocks.stream().filter(c -> c.getLevel(false).equals(String.valueOf(level))).findFirst().get();
    }

    /**
     * {@link  borders}
     * @return particles showed to player when asked
     */
    public Map<Long, Player> getBorders() {
        return borders;
    }

    /**
     * This is the method called when the plugin is enabled.
     * This will
     *
     * initialize the {@link #borderAPI} variable,
     * initialize the {@link #headUtil} variable,
     * register the {@link FastInvManager},
     * register the tasks,
     * register blocks,
     * register heads {@link HeadUtil},
     * register the events.
     */
    @Override
    public void onEnable() {
        borderAPI = BorderAPI.getInstance(this);
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
}
