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

import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import org.bukkit.Location;
import org.bukkit.World;

public class BorderFinder {
    private BorderFinder() {}
    
    public static interface Region {
        public boolean contains(BorderPoint point);
        public boolean contains(int x, int y, int z);
        public Collection<BorderPoint> getPoints();
        public World getWorld();
    }
    
    public static Collection<Location> getBorderPoints(final ProtectedRegion wgRegion, World world) {
        Region region = new ProtectedRegionRegion(world, wgRegion);
        Collection<BorderPoint> rawBorderPoints = getBorderPoints(region);
        HashSet<Location> borderPoints = new HashSet<>();
        for (BorderPoint borderPoint : rawBorderPoints) {
            borderPoints.add(borderPoint.asLocation());
        }
        return borderPoints;
    }
    
    public static Collection<BorderPoint> getBorderPoints(Region region) {
        HashSet<BorderPoint> result = new HashSet<>();
        for (BorderPoint point : region.getPoints()) {
            getAlongX(point, region, result);
            getAlongZ(point, region, result);
        }
        return result;
    }
    
    private static void getAlongX(BorderPoint start, Region region, HashSet<BorderPoint> result) {
        if (region.contains(start.getX() + 1, start.getY(), start.getZ())) { //We are positive
            for (int x = start.getX(); region.contains(x, start.getY(), start.getZ()); x++) {
                result.add(new BorderPoint(x, start.getY(), start.getZ(), region.getWorld()));
            }
        } else { //We are negative or one block
            for (int x = start.getX(); region.contains(x, start.getY(), start.getZ()); x--) {
                result.add(new BorderPoint(x, start.getY(), start.getZ(), region.getWorld()));
            }
        }
    }
    
    private static void getAlongZ(BorderPoint start, Region region, HashSet<BorderPoint> result) {
        if (region.contains(start.getX(), start.getY(), start.getZ() + 1)) { //We are positive
            for (int z = start.getZ(); region.contains(start.getX(), start.getY(), z); z++) {
                result.add(new BorderPoint(start.getX(), start.getY(), z, region.getWorld()));
            }
        } else { //We are negative or one block
            for (int z = start.getZ(); region.contains(start.getX(), start.getY(), z); z--) {
                result.add(new BorderPoint(start.getX(), start.getY(), z, region.getWorld()));
            }
        }
    }
    
    public static class BorderPoint {
        private final int x, y, z;
        private final World world;
        
        public BorderPoint(int x, int y, int z, World world) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.world = world;
        }
        
        @Override
        public boolean equals(Object otherObj) {
            if (otherObj == null) return false;
            if (otherObj == this) return true;
            if (otherObj instanceof BorderPoint) {
                BorderPoint other = (BorderPoint) otherObj;
                if (other.x != this.x || other.y != this.y || other.z != this.z) return false;
                if (this.world == null && other.world != null) return false;
                return this.world.equals(other.world);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            int result = 19;
            result = 31 * result + x;
            result = 31 * result + y;
            result = 31 * result + z;
            result = 31 * result + world.hashCode();
            return result;
        }
        
        public Location asLocation() {
            return new Location(world, x, y, z);
        }
        
        public int getX() {
            return x;
        }
        
        public int getY() {
            return y;
        }
        
        public int getZ() {
            return z;
        }
        
        public World getWorld() {
            return world;
        }
    }
}
