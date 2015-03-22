package net.techcable.combattag.concurrent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * An immutable snapshot of a block's data
 *
 * @author Techcable
 */
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class BlockInfo {
    private final BlockPos position;
    private final Material material;
    private final byte data;

    public final static BlockInfo fromBlock(Block block) {
        return new BlockInfo(BlockPos.fromLocation(block.getLocation()), block.getType(), block.getData());
    }

    public static BlockInfo getAtPosition(BlockPos pos) {
        return fromBlock(pos.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()));
    }
}
