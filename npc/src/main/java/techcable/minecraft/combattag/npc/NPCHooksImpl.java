package techcable.minecraft.combattag.npc;

import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.citizensnpcs.api.npc.NPC;

import com.avaje.ebeaninternal.server.persist.dml.UpdatePlan;
import com.trc202.CombatTag.CombatTag;

import techcable.minecraft.combattag.NPCHooks;

import lombok.*;

@Getter
@RequiredArgsConstructor
public class NPCHooksImpl extends NPCHooks {
	private NPCMaster npcMaster;
	private final CombatTag plugin;
	private NPCListener listener;
	
	@Override
	public void init() {
		if (!isNPCEnabled()) return;
		npcMaster = new NPCMaster(getPlugin());
		listener = new NPCListener(this);
		Bukkit.getPluginManager().registerEvents(listener, getPlugin());
	}

	@Override
	public void onDisable() {
		if (!isNPCEnabled()) return;
		wipeAll();
	}
	
	@Override
	public boolean isNPC(Entity entity) {
		if (isNPCEnabled()) {
			return getNpcMaster().isNPC(entity);
		} else {
			return super.isNPC(entity);
		}
	}
	
	public int wipeAll() {
		int numWiped = 0;
		for (NPC npc : getNpcMaster().getNpcs()) {
			despawnNPC(npc);
			numWiped++;
		}
		return numWiped;
	}
	public NPC getNPC(UUID player) {
		return npcMaster.getNPC(player);
	}
	
	public boolean isTagged(UUID player) {
		return getPlugin().inTagged(player);
	}
	
	public void despawnNPC(NPC npc) {
		updatePlayerData(npc);
		npcMaster.despawn(npc);
	}
	
	public void despawnNPC(UUID player) {
		NPC npc = getNpcMaster().getNPC(player);
		despawnNPC(npc);
	}
	
	public void addTagged(Player tagged) {
		getPlugin().addTagged(tagged);
	}
	
	public void removeTagged(UUID tagged) {
		getPlugin().removeTagged(tagged);
	}
	
	public boolean isNPCEnabled() {
		return !getPlugin().settings.isInstaKill();
	}

	public NPC spawnNPC(Player player, Location location) {
		if (isDebugEnabled()) {
            		getLog().info("[CombatTag] Spawning NPC for " + player.getName());
        	}
        	NPC npc = getNpcMaster().createNPC(player);
       		npc.spawn(location);
        	return npc;
	}

	public boolean isDebugEnabled() {
	    return getPlugin().isDebugEnabled();
	}

	public Logger getLog() {
	    return getPlugin().log;
	}
	
	public void updatePlayerData(UUID player) {
		NPC npc = getNpcMaster().getNPC(player);
		if (npc == null) {
			return;
		}
		NPCUtils.updatePlayerData(npc, player);
	}
	
	public void updatePlayerData(NPC npc) {
		 UUID player = npcMaster.getPlayerId(npc);
		 updatePlayerData(player);
	}
	
	public boolean isNPCDespawnAfterTime() {
		return getNPCDespawnTime() > 0;
	}
	
	public int getNPCDespawnTime() {
		return getPlugin().settings.getNpcDespawnTime();
	}

	public boolean isNPCDieWhenDespawn() {
		return getPlugin().settings.isNpcDieAfterTime();
	}
	
	public void scheduleDelayedDespawn(NPC npc) {
		UUID realPlayer = npcMaster.getPlayerId(npc);
		long despawnTicks = getNPCDespawnTime() * 20;
		Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), new NPCDelayedDespawnTask(realPlayer, this, npc), despawnTicks);	
	}
	
	public double healthCheck(double original) {
		return getPlugin().healthCheck(original);
	}
}