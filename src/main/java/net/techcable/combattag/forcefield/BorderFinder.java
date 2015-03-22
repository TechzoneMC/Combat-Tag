/**
 * (c) 2015 Nicholas Schlabach
 * 
 * You are free to use this class as long as you meet the following conditions
 * 
 * 1. You may not charge money for access to any software conaining or accessing this class
 * 2. Any software containing or accessing this class must be available to the public on either dev.bukkit.org or spigotmc.org
 * 3. ALL source code for any piece of software using this class must be given out
 */
package net.techcable.combattag.forcefield;

import java.util.Collection;
import java.util.HashSet;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.techcable.combattag.concurrent.BlockPos;
import org.bukkit.Location;
import org.bukkit.World;

public class BorderFinder {
    private BorderFinder() {}
    
    public static interface Region {
        public boolean contains(BlockPos point);
        public boolean contains(int x, int y, int z);
        public Collection<BlockPos> getPoints();
        public World getWorld();
        public BlockPos getMin();
        public BlockPos getMax();
    }
    
    public static Collection<Location> getBlockPoss(final ProtectedRegion wgRegion, World world) {
        Region region = new ProtectedRegionRegion(world, wgRegion);
        Collection<BlockPos> rawBlockPoss = getBorderPoints(region);
        HashSet<Location> BlockPoss = new HashSet<>();
        for (BlockPos BlockPos : rawBlockPoss) {
            BlockPoss.add(BlockPos.asLocation());
        }
        return BlockPoss;
    }
    
    public static Collection<BlockPos> getBorderPoints(Region region) {
        HashSet<BlockPos> result = new HashSet<>();
        for (BlockPos point : region.getPoints()) {
            getAlongX(point, region, result);
            getAlongZ(point, region, result);
        }
        return result;
    }
    
    private static void getAlongX(BlockPos start, Region region, HashSet<BlockPos> result) {
        if (region.contains(start.getX() + 1, start.getY(), start.getZ())) { //We are positive
            for (int x = start.getX(); region.contains(x, start.getY(), start.getZ()); x++) {
                result.add(new BlockPos(x, start.getY(), start.getZ(), region.getWorld()));
            }
        } else { //We are negative or one block
            for (int x = start.getX(); region.contains(x, start.getY(), start.getZ()); x--) {
                result.add(new BlockPos(x, start.getY(), start.getZ(), region.getWorld()));
            }
        }
    }
    
    private static void getAlongZ(BlockPos start, Region region, HashSet<BlockPos> result) {
        if (region.contains(start.getX(), start.getY(), start.getZ() + 1)) { //We are positive
            for (int z = start.getZ(); region.contains(start.getX(), start.getY(), z); z++) {
                result.add(new BlockPos(start.getX(), start.getY(), z, region.getWorld()));
            }
        } else { //We are negative or one block
            for (int z = start.getZ(); region.contains(start.getX(), start.getY(), z); z--) {
                result.add(new BlockPos(start.getX(), start.getY(), z, region.getWorld()));
            }
        }
    }
}
