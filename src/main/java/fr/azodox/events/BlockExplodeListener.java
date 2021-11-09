package fr.azodox.events;

import fr.azodox.util.CBlockUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.List;
import java.util.Optional;

public class BlockExplodeListener implements Listener {

    @EventHandler
    public void onBlockExplode(EntityExplodeEvent e){
        List<Block> blocks = e.blockList();
        Optional<Block> optionalBlock = blocks.stream().filter(b -> b.getType().equals(Material.GOLD_BLOCK)).findFirst();
        if (!optionalBlock.isPresent()) {
            return;
        }

        Block block = optionalBlock.get();

        if(CBlockUtil.isAClaimBlock(block)){
            e.blockList().remove(block);
        }
    }
}
