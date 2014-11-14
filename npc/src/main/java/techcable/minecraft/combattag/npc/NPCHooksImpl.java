package techcable.minecraft.combattag.npc;

import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.citizensnpcs.api.npc.NPC;

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
		 npcMaster = new NPCMaster(plugin);
		 listener = new NPCListener(this);
	}
	
	public NPC getNpc(UUID player) {
		return npcMaster.getNPC(player);
	}
	
	public boolean isTagged(UUID player) {
		return plugin.inTagged(player);
	}
	
	public void despawnNPC(NPC npc) {
		npcMaster.despawn(npc);
	}
	
	public void addTagged(Player tagged) {
		plugin.addTagged(tagged);
	}
	
	public void removeTagged(UUID tagged) {
		plugin.removeTagged(tagged);
	}
	
	public boolean isNPCEnabled() {
		return !plugin.settings.isInstaKill();
	}

	public void spawnNPC(Player player, Location location) {
		if (isDebugEnabled()) {
            getLog().info("[CombatTag] Spawning NPC for " + plr.getName());
        }
        NPC npc = getNpcMaster().createNPC(plr);
        npc.spawn(location);
        return npc;
	}
	
	public boolean isDebugEnabled() {
	    return plugin.isDebugEnabled();
	}
	
	public Logger getLog() {
	    return plugin.log;
	}
}
