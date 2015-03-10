package net.techcable.combattag;

import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.techcable.npclib.NPC;
import net.techcable.npclib.NPCLib;
import net.techcable.npclib.NPCRegistry;
import net.techcable.techutils.inventory.InventoryUtils;
import net.techcable.techutils.inventory.PlayerData;
import lombok.*;

@RequiredArgsConstructor
@Getter
public class NPCManager {
	private final CombatTag plugin;
	
	public NPC spawnNPC(CombatPlayer toSpawnFor) {
		NPC npc = getRegistry().createNPC(EntityType.PLAYER, toSpawnFor.getId(), toSpawnFor.getNpcName());
		npc.setSkin(toSpawnFor.getId());
		npc.setProtected(false);
		npc.spawn(toSpawnFor.getEntity().getLocation());
		copyFromPlayer(npc, toSpawnFor.getEntity());
		return npc;
	}
	
	public void despawn(NPC npc) {
		syncInventory(npc);
		npc.despawn();
		getRegistry().deregister(npc);
		Utils.debug("Despawned " + npc.getName());
	}
	
	public NPC getNpc(UUID id) {
		return getRegistry().getByUUID(id);
	}
	
	public NPC getNpc(Entity e) {
		return getRegistry().getAsNPC(e);
	}
	
	private static void copyFromPlayer(NPC npc, Player source) {
		InventoryUtils.copy(InventoryUtils.getData(source), InventoryUtils.getData((Player)npc.getEntity()));
	}
	
	public static void syncInventory(NPC npc) {
		PlayerData player = InventoryUtils.getData(npc.getUUID());
		InventoryUtils.copy(InventoryUtils.getData((Player)npc.getEntity()), player);
	}
	
	public NPCRegistry getRegistry() {
		return NPCLib.getNPCRegistry("Combat-Tag", getPlugin());
	}
	
	public boolean isNPC(Entity e) {
		return getRegistry().isNPC(e);
	}
	
	public static NPCManager getInstance() {
		return Utils.getPlugin().getNpcManager();
	}
	
	public void delayedDespawn(final NPC npc, int despawnTicks, final boolean kill) {
		BukkitRunnable task = new BukkitRunnable() {
			
			@Override
			public void run() {
				if (kill) {
					((LivingEntity)npc.getEntity()).damage(1000);
				} else {
					despawn(npc);
				}
			}
		};
		task.runTaskLater(Utils.getPlugin(), despawnTicks);
	}
	
	private int npcNumber;
	public int getNpcNumber() {
		return npcNumber++;
	}
}