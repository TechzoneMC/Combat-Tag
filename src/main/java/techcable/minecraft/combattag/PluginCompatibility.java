package techcable.minecraft.combattag;

import me.libraryaddict.disguise.DisguiseAPI;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;

import techcable.minecraft.techutils.libs.factionsapi.FactionsAPI;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import lombok.*;

@Getter
public class PluginCompatibility {
	private PluginCompatibility() {}
    
    public static boolean isPvpDisabled(Location location) {
    	return !isWGPvPEnabled(location) || isSafezone(location);
    }
        public static boolean isWGPvPEnabled(Location location) {
	    if (!hasWG()) return true;
	    ApplicableRegionSet set = WGBukkit.getRegionManager(location.getWorld()).getApplicableRegions(location);
	    return set.testState(null, DefaultFlag.PVP);
	}
    
    public static boolean isSafezone(Location location) {
    	if (!hasFactions()) return false;
    	return FactionsAPI.getInstance().getOwningFaction(location).isSafezone();
    }
    
    public static boolean hasWG() {
	try {
	    Class.forName("com.sk89q.worldguard.bukkit.WorldGuardPlugin");
	} catch (ClassNotFoundException ex) {
	    return false;
	}
	Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
	if (plugin != null && plugin instanceof WorldGuardPlugin) return true;
	else return false;
    }
    
    public static boolean isDisguised(Player player) {
        if (!hasLibsDisguises()) return false;
        return DisguiseAPI.isDisguised(player);
    }
    
    public static void unDisguise(Player player) {
        if (!hasLibsDisguises()) return;
        DisguiseAPI.undisguiseToAll(player);
    }
    
    public static boolean hasFactions() {
    	return FactionsAPI.isFactionsInstalled();
    }
	
	public static boolean hasLibsDisguises() {
	    try {
	        Class.forName("me.libraryaddict.disguise.DisguiseAPI");
	        return true;
	    } catch (ClassNotFoundException ex) {
	        return false;
	    }
	}
}
