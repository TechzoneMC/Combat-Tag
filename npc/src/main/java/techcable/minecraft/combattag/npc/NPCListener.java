package techcable.minecraft.combattag.npc;

import java.util.UUID;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import com.trc202.CombatTag.CombatTag;

import techcable.minecraft.combattag.Utils;
import techcable.minecraft.combattag.events.PvPLogEvent;

import lombok.*;

@RequiredArgsConstructor
public class NPCListener {

	private final NPCHooksImpl hooks;
	
	public void onJoin(PlayerJoinEvent event) {
		Player loginPlayer = event.getPlayer();
		UUID playerUUID = loginPlayer.getUniqueId();
		if (hooks.getNPC(playerUUID) == null) {
			return;
		}
		if (hooks.isTagged(playerUUID)) {
			loginPlayer.setNoDamageTicks(0);
			hooks.despawnNPC(playerUUID);
			if (loginPlayer.getHealth() > 0) {
				hooks.addTagged(loginPlayer);
			} else {
				hooks.removeTagged(playerUUID);
			}
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPvpLog(PvPLogEvent event) {
		if (!hooks.isNPCEnabled()) return;
		Player logger = event.getPlayer();
		boolean shouldSpawn = true;
		if (shouldSpawn) {
			NPC npc = hooks.spawnNpc(logger, logger.getLocation());
			Player npcPlayer = (Player) npc.getEntity();
			Utils.copyNPC(npc, logger);
			npcPlayer.setHealth(plugin.healthCheck(logger.getHealth()));
			logger.getWorld().createExplosion(logger.getLocation(), -1); // Create the smoke effect
			if (plugin.settings.getNpcDespawnTime() > 0) {
				plugin.scheduleDelayedKill(npc, logger.getUniqueId());
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player loginPlayer = event.getPlayer();
		UUID playerUUID = loginPlayer.getUniqueId();
		if (hooks.getNPC(playerUUID) == null) {
			return;
		}
		if (hooks.isTagged(playerUUID)) {
			// Player has an NPC and is likely to need some sort of punishment
			loginPlayer.setNoDamageTicks(0);
			hooks.despawnNPC(playerUUID);
			if (loginPlayer.getHealth() > 0) {
				hooks.addTagged(loginPlayer);
			} else {
				hooks.removeTagged(playerUUID);
			}
		}
	}
}