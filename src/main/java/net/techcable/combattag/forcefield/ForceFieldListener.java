package net.techcable.combattag.forcefield;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import net.techcable.combattag.concurrent.BlockInfo;
import net.techcable.combattag.concurrent.BlockPos;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.techcable.combattag.CombatTag;
import net.techcable.combattag.forcefield.BorderFinder.Region;

import lombok.*;

@RequiredArgsConstructor
public class ForceFieldListener implements Listener {
    @Getter
    private final CombatTag plugin;

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        ForceField field = plugin.getPlayer(event.getPlayer()).getForceField(this);
        field.updateForceField(getAllWorguardRegionsWithoutPvp());
    }

    private HashSet<Region> allWorldguardRegionsWithoutPvp;
    public Collection<? extends Region> getAllWorguardRegionsWithoutPvp() {
        if (allWorldguardRegionsWithoutPvp == null) {
            allWorldguardRegionsWithoutPvp = new HashSet<>();
            for (World world : Bukkit.getWorlds()) {
                Collection<ProtectedRegion> all = WGBukkit.getRegionManager(world).getRegions().values();
                for (ProtectedRegion rawRegion : all) {
                    if (!StateFlag.test(rawRegion.getFlag(DefaultFlag.PVP))) continue; //Pvp is allowed
                    BorderFinder.Region region = new ProtectedRegionRegion(world, rawRegion);
                    allWorldguardRegionsWithoutPvp.add(region);
                }
            }
        }
        return allWorldguardRegionsWithoutPvp;
    }

    private final ConcurrentMap<BlockPos, BlockInfo> lastKnownBlockData = new ConcurrentHashMap<>();

    public BlockInfo getInfoAt(BlockPos pos) {
        return lastKnownBlockData.get(pos);
    }

    public void takeBlockSnapshot(BlockPos pos) {
        lastKnownBlockData.put(pos, BlockInfo.getAtPosition(pos));
    }

    public void cacheAll() { //Cache everything we can to increase speed in the future, very important to call
        getAllWorguardRegionsWithoutPvp();
        for (BorderFinder.Region region : getAllWorguardRegionsWithoutPvp()) {
            for (BlockPos borderPoint : ForceField.getBorderPoints(region)) {
                for (int y = 0; y < region.getWorld().getMaxHeight(); y++) {
                    takeBlockSnapshot(borderPoint); //We will only need to see the border point data :)
                }
            }
        }
    }

    @EventHandler
    public void onBlockChange(BlockEvent event) {
        BlockPos position = BlockPos.fromLocation(event.getBlock().getLocation());
        if (lastKnownBlockData.containsKey(position)) {
            lastKnownBlockData.put(position, BlockInfo.fromBlock(event.getBlock()));
        }
    }
}