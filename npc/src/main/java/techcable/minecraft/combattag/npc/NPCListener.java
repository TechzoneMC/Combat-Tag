package techcable.minecraft.combattag.npc;

import java.util.UUID;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import com.trc202.CombatTag.CombatTag;

import techcable.minecraft.combattag.Utils;
import techcable.minecraft.combattag.events.PvPLogEvent;

import lombok.*;

@RequiredArgsConstructor
public class NPCListener {
    
    private final CombatTag plugin;
    
    public void onJoin(PlayerJoinEvent event) {
	Player loginPlayer = event.getPlayer();
	UUID playerUUID = loginPlayer.getUniqueId();
	if (plugin.getNpcMaster().getNPC(playerUUID) == null) {
	    return;
	}
	if (plugin.inTagged(playerUUID)) {
	    loginPlayer.setNoDamageTicks(0);
	    plugin.despawnNPC(playerUUID);
	    if (loginPlayer.getHealth() > 0) {
		plugin.addTagged(loginPlayer);
	    } else {
		plugin.removeTagged(playerUUID);
	    }
	}
    }

    public void onPvpLog(PvPLogEvent event) {
	Player logger = event.getPlayer();
	boolean shouldSpawn = true;
	if (plugin.settings.dontSpawnInWG()) {
	    //shouldSpawn = plugin.ctIncompatible.InWGCheck(quitPlr);
	}
	if (shouldSpawn) {
	    NPC npc = plugin.spawnNpc(logger, logger.getLocation());
	    Player npcPlayer = (Player) npc.getEntity();
	    Utils.copyNPC(npc, logger);
	    npcPlayer.setHealth(plugin.healthCheck(logger.getHealth()));
	    logger.getWorld().createExplosion(logger.getLocation(), -1); //Create the smoke effect
	    if (plugin.settings.getNpcDespawnTime() > 0) {
		plugin.scheduleDelayedKill(npc, logger.getUniqueId());
	    }
	}
    }
}