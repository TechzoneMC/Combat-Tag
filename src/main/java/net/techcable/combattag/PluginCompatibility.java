package net.techcable.combattag;

import me.libraryaddict.disguise.DisguiseAPI;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.flags.DefaultFlag;

import lombok.*;

@Getter
public class PluginCompatibility {
	private PluginCompatibility() {}
    
    public static boolean isPvpDisabled(Location location) {
    	return !isWGPvPEnabled(location);
    }
    
    public static boolean isWGPvPEnabled(Location location) {
    	return WGBukkit.getRegionManager(location.getWorld()).getApplicableRegions(location).allows(DefaultFlag.PVP);
	}
    
    public static boolean isDisguised(Player player) {
        if (!hasLibsDisguises()) return false;
        return DisguiseAPI.isDisguised(player);
    }
    
    public static void unDisguise(Player player) {
        if (!hasLibsDisguises()) return;
        DisguiseAPI.undisguiseToAll(player);
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
