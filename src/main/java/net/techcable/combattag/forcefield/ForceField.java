package net.techcable.combattag.forcefield;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.*;
import net.techcable.combattag.concurrent.BlockInfo;
import net.techcable.combattag.concurrent.BlockPos;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.techcable.combattag.forcefield.BorderFinder.Region;

import lombok.*;

@Getter
public class ForceField extends BukkitRunnable {

    public static final int DEFAULT_RADIUS = 20;
    private final ForceFieldListener listener;

    public ForceField(Player player, ForceFieldListener listener) {
        this.player = player;
        this.plugin = listener.getPlugin();
        this.listener = listener;
        runTaskTimerAsynchronously(plugin, 0, 3); //Every 3 ticks
    }
    
    private final Player player;
    private final Plugin plugin;
    private volatile BlockPos lastKnownLocation;

    private final Multimap<Region, BlockPos> toUpdate = Multimaps.synchronizedSetMultimap(MultimapBuilder.SetMultimapBuilder.hashKeys().hashSetValues().<Region, BlockPos>build());

    private volatile Multimap<Region, BlockPos> previouslyUpdated;
    /**
     * Create a forcefield around the border of a region
     * 
     * <b>Very Important for Regions to implement the Equals and hashCode Methods</b>
     * 
     * Only show within the specified radius of the player
     *
     * @param regions regions that the border should show along the border of
     */
    public void updateForceField(Collection<? extends BorderFinder.Region> regions) {
        lastKnownLocation = BlockPos.fromLocation(player.getLocation());
        for (Region region : regions) {
            for (BlockPos point : getBorderPoints(region)) {
                this.toUpdate.put(region, point);
            }
        }
    }
    
    private static final Multimap<BorderFinder.Region, BlockPos> borderCache = HashMultimap.create();
    public static Collection<BlockPos> getBorderPoints(BorderFinder.Region region) {
        if (!borderCache.containsKey(region)) {
            Collection<BlockPos> BlockPoss = BorderFinder.getBorderPoints(region);
            borderCache.putAll(region, BlockPoss);
        }
        return borderCache.get(region);
    }
    
    public boolean inRadius(int radius, BlockPos l) { //Based on worldguard code
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
        if (previouslyUpdated != null) {
            for (Region region : previouslyUpdated.keySet()) {
                for (BlockPos borderPoint : previouslyUpdated.get(region)) {
                    for (int y = 0; y < region.getWorld().getMaxHeight(); y++) { //getMaxHeight is thread safe
                        BlockPos position = borderPoint.withY(y);
                        BlockInfo info = listener.getInfoAt(position);
                        player.sendBlockChange(position.asLocation(), info.getMaterial(), info.getData());
                    }
                }
            }
        }

        ImmutableMultimap<Region, BlockPos> toUpdate = ImmutableMultimap.copyOf(this.toUpdate);
        BlockPos lastKnownLocation = this.lastKnownLocation;
        for (Region region : toUpdate.keySet()) {
            for (BlockPos borderPoint : toUpdate.get(region)) {
                for (int y = region.getMin().getY(); y <= region.getMax().getY(); y++) {
                    BlockPos position = borderPoint.withY(y);
                    BlockInfo realData = listener.getInfoAt(position);
                    if (!realData.getMaterial().isSolid() && inRadius(DEFAULT_RADIUS, lastKnownLocation)) {
                        player.sendBlockChange(position.asLocation(), Material.STAINED_GLASS_PANE, (byte) 14);
                    }
                }
            }
        }

        this.previouslyUpdated = toUpdate;
    }
}
