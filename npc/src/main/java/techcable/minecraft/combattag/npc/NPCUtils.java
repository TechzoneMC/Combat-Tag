package techcable.minecraft.combattag.npc;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.citizensnpcs.api.npc.NPC;

import com.google.common.base.Throwables;

import techcable.minecraft.combattag.Utils;
import techcable.minecraft.offlineplayers.AdvancedOfflinePlayer;
import techcable.minecraft.offlineplayers.NBTAdvancedOfflinePlayer;
import techcable.minecraft.offlineplayers.NBTAdvancedOfflinePlayer.PlayerNotFoundException;
import techcable.minecraft.offlineplayers.wrapper.OnlineAdvancedOfflinePlayer;

public class NPCUtils {
    private NPCUtils() {}
    
    public static void updatePlayerData(NPC npc, UUID playerUUID) {
    	AdvancedOfflinePlayer target;
    	Player source = (Player) npc.getEntity();
    	if (Bukkit.getPlayer(playerUUID) == null) {
    		try {
    			target = new NBTAdvancedOfflinePlayer(Bukkit.getOfflinePlayer(playerUUID));
    		} catch (PlayerNotFoundException ex) {
    			throw Throwables.propagate(ex);
    		}
    		target.load();
    	} else {
    		target = new OnlineAdvancedOfflinePlayer(Bukkit.getPlayer(playerUUID));
    	}
    	if (source.getHealth() <= 0) {
    		Utils.emptyInventory(target);
    		target.setHealth(0);
    	} else {
    		Utils.copyPlayer(target, source);
    	}
    	target.save();
    }
}