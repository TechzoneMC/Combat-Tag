package net.techcable.combattag.forcefield;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

import com.google.common.collect.Collections2;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.techcable.combattag.CombatTag;
import net.techcable.combattag.forcefield.BorderFinder.BorderPoint;
import net.techcable.combattag.forcefield.BorderFinder.Region;

import lombok.*;

@RequiredArgsConstructor
public class ForceFieldListener implements Listener {
    private final CombatTag plugin;

    public void onMove(PlayerMoveEvent event) {
        ForceField field = plugin.getPlayer(event.getPlayer()).getForceField();
        field.updateForceField(20, getAllWorguardRegionsWithoutPvp(), Material.STAINED_GLASS_PANE, (byte)14);
    }

    private HashSet<? extends Region> allWorldguardRegionsWithoutPvp;
    public Collection<? extends Region> getAllWorguardRegionsWithoutPvp() {
        if (allWorldguardRegionsWithoutPvp == null) {
            allWorldguardRegionsWithoutPvp = new HashSet<>();
            for (World world : Bukkit.getWorlds()) {
                Collection<ProtectedRegion> all = WGBukkit.getRegionManager(world).getRegions().values();
                for (ProtectedRegion rawRegion : all) {
                    if (stateFlag.test(rawRegion.getFlag(DeafultFlag.PVP))) continue; //Pvp is allowed
                    BorderFinder.Region region = new ProtectedRegionRegion(world, rawRegion);
                    allWorldguardRegionsWithoutPvp.add(region);
                }
            }
        }
        return allWorldguardRegionsWithoutPvp;
    }
    
    public void cacheAll() { //Cache everything we can to increase speed in the future, very important to call
        getAllWorguardRegionsWithoutPvp();
        for (BorderFinder.Region region : getAllWorguardRegionsWithoutPvp()) {
            ForceField.getBorderPoints(region);
        }
    }
}