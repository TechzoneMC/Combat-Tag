 package techcable.minecraft.combattag.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.techcable.minecraft.techutils.offlineplayer.PlayerData;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.base.Preconditions;

import techcable.minecraft.combattag.Utils;
import static techcable.minecraft.combattag.Utils.getPlugin;
import techcable.minecraft.npclib.NPC;
import techcable.minecraft.npclib.NPCLib;
import techcable.minecraft.npclib.NPCRegistry;
import techcable.minecraft.techutils.InventoryUtils;
import techcable.minecraft.techutils.UUIDUtils;
import techcable.minecraft.techutils.utils.EasyCache;
import techcable.minecraft.techutils.utils.EasyCache.Loader;
import lombok.*;

@Getter
@RequiredArgsConstructor
public class CombatTagNPC {
	private final UUID npcId;
	
	private static final NPCRegistry registry = NPCLib.getNPCRegistry("CombatTagReloaded");
	
	public Player getAsPlayer() {
		return (Player) getNpc().getEntity();
	}
	
	public CombatTagPlayer getPlayer() {
		return CombatTagPlayer.getPlayer(getNpc().getUUID());
	}
	
	public void despawn() {
		syncInventory();
		if (getNpc().isSpawned()) getNpc().despawn();
		Utils.debug("Despawned npc for " + getPlayer().getName());
	}
	
	public void spawn(Location toSpawn) {
		getNpc().spawn(toSpawn);
	}
	

	public void syncInventory() {
		PlayerData player = getPlugin().getPlayerData(getPlayer().getUuid());
		if (getNpc().getEntity().isDead()) {
			InventoryUtils.emptyInventory(player);
		} else {
			PlayerData npcPlayer = getPlugin().getPlayerData(getAsPlayer());
			InventoryUtils.copy(npcPlayer, player);
		}
	}

	public void scheduleDelayedDespawn(long delay, boolean kill) {
		NPCDelayedDespawn task = new NPCDelayedDespawn(this, kill);
		task.runTaskLater(Utils.getPlugin(), delay);
	}
	
	public NPC getNpc() {
		return registry.getByUUID(getNpcId());
	}
	
	@RequiredArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class NPCDelayedDespawn extends BukkitRunnable {
		private final CombatTagNPC npc;
		private boolean kill;
		@Override
		public void run() {
			if (kill) {
				getNpc().getAsPlayer().setHealth(0);
				getNpc().syncInventory();
			} else {
				getNpc().despawn();
			}
		}
		
	}
	
	
	private static EasyCache<UUID, CombatTagNPC> npcCache = EasyCache.makeCache(new Loader<UUID, CombatTagNPC>() {

		@Override
		public CombatTagNPC load(UUID key) {
			return new CombatTagNPC(key);
		}
		
	});
	public static CombatTagNPC getNPC(UUID player) {
		return npcCache.get(player);
	}
	public static CombatTagNPC createNPC(UUID player) {
		if (registry.getByUUID(player) == null) {
			registry.createNPC(EntityType.PLAYER, player, UUIDUtils.getName(player));
		}
		return getNPC(player);
	}
	public static List<CombatTagNPC> getAllNpcs() {
		List<CombatTagNPC> npcs = new ArrayList<>();
		for (NPC npc : registry.listNpcs()) {
			npcs.add(getNPC(npc.getUUID()));
		}
		return npcs;
	}
	public static boolean isNPC(Entity entity) {
		return registry.isNPC(entity);
	}
}
