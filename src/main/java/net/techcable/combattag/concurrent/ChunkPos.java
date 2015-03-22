package net.techcable.combattag.concurrent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * An immutable chubk position
 *
 * @author Techcable
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class ChunkPos {
    private final int x, z;
    private final World world;

    public int getAbsoluteX(int relativeX) {
        return fromRelative(getX(), relativeX);
    }

    public int getAbsoluteZ(int absoluteZ) {
        return fromRelative(getZ(), absoluteZ);
    }

    public static ChunkPos fromChunk(Chunk chunk) {
        return new ChunkPos(chunk.getX(), chunk.getZ(), chunk.getWorld());
    }

    public static ChunkPos fromLocation(Location l) {
        return new ChunkPos(l.getBlockX() >> 4, l.getBlockZ() >> 4, l.getWorld());
    }

    public static int toRelative(int absolute) {
        return absolute & 0xF; //First 16 bits
    }

    public static int fromRelative(int chunk, int relative) {
        return (chunk << 4) | (relative & 0xF);
    }
}