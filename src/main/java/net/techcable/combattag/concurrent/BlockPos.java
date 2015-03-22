package net.techcable.combattag.concurrent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * An immutable block position
 *
 * @author Techcable
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class BlockPos {
    private final int x, y, z;
    private final World world;

    public BlockPos withY(int y) {
        return new BlockPos(x, y, z, world);
    }

    public Location asLocation() {
        return new Location(world, x, y, z);
    }

    public static BlockPos fromLocation(Location l) {
        return new BlockPos(l.getBlockX(), l.getBlockY(), l.getBlockZ(), l.getWorld());
    }

    public ChunkPos toChunk() {
        return new ChunkPos(x >> 4, z >> 4, world);
    }
}