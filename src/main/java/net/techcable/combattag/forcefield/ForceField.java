package net.techcable.combattag.forcefield;

import java.util.Collection;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.sk89q.worldguard.protection.flags.DefaultFlag;

import net.techcable.combattag.forcefield.BorderFinder.BorderPoint;
import net.techcable.combattag.forcefield.BorderFinder.Region;

import lombok.*;

@Getter
public class ForceField extends BukkitRunnable {
    
    public ForceField(Player player, Plugin plugin) {
        this.player = player;
        this.plugin = plugin;
        this.runTaskTimerAsynchronously(plugin, 1, 1);
    }
    
    private final Player player;
    private final Plugin plugin;
    private final Queue<BlockInfo> toSend = new ConcurrentLinkedQueue<>();
    private final Queue<BlockInfo> toReset = new ConcurrentLinkedQueue<>();
    
    private Set<BlockInfo> previouslyUpdated;
    
    /**
     * Create a forcefield around the border of a region
     * 
     * <b>Very Important for Regions to implement the Equals and hashCode Methods</b>
     * 
     * Only show within the specified radius of the player
     * 
     * @param radius to show border in
     * @param regions regions that the border should show along the border of
     * @param material the material that will show
     * @param data the data of the material that will show
     */
    public void updateForceField(int radius, Collection<? extends BorderFinder.Region> regions, Material material, byte data) {
        Set<BlockInfo> toUpdate = new HashSet<>();
        
        for (Region region : regions) {
            for (BorderPoint point : getBorderPoints(region)) {
                if (inRadius(radius, point)) {
                    toUpdate.add(new BlockInfo(material, data, point));
                }
            }
        }
        
        toSend.addAll(toUpdate);
        if (previouslyUpdated != null) {
            for (BlockInfo info : previouslyUpdated) {
                Block block = info.getLocation().asLocation().getBlock();
                toReset.add(new BlockInfo(block.getType(), block.getData(), info.getLocation())); //Reset to real info
            }
        }
        previouslyUpdated = toUpdate;
    }
    
    private static final Multimap<BorderFinder.Region, BorderFinder.BorderPoint> borderCache = HashMultimap.create();
    public static Collection<BorderPoint> getBorderPoints(BorderFinder.Region region) {
        if (!borderCache.containsKey(region)) {
            Collection<BorderFinder.BorderPoint> borderPoints = BorderFinder.getBorderPoints(region);
            borderCache.putAll(region, borderPoints);
        }
        return borderCache.get(region);
    }
    
    public boolean inRadius(int radius, BorderPoint l) { //Based on worldguard code
        Location corner1 = getPlayer().getLocation().add(radius, 0, radius);
        Location corner2 = getPlayer().getLocation().subtract(radius, 0, radius);
        int x = l.getX();
        int y = l.getY();
        int z = l.getZ();
        return x >= corner1.getBlockX() && x < corner2.getBlockX()+1
                && y >= corner1.getBlockY() && y < corner2.getBlockY()+1
                && z >= corner1.getBlockZ() && z < corner2.getBlockZ()+1;
    }
    
    @Override
    public void run() {
        while (!toReset.isEmpty()) {
            BlockInfo info = toReset.poll();
            getPlayer().sendBlockChange(info.getLocation().asLocation(), info.getMaterial(), info.getData());
        }
        while (!toSend.isEmpty()) {
            BlockInfo info = toSend.poll();
            getPlayer().sendBlockChange(info.getLocation().asLocation(), info.getMaterial(), info.getData());
        }
    }
    
    @RequiredArgsConstructor
    @AllArgsConstructor
    @Getter
    private static class BlockInfo {
        private final Material material;
        private final byte data;
        private BorderFinder.BorderPoint location;
    }
}
