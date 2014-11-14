package techcable.minecraft.combattag.npc;

import java.util.UUID;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.*;

@Getter
@RequiredArgsConstructor
public class NPCDelayedDespawnTask extends BukkitRunnable {
	private final UUID realPlayer;
	private final NPCHooksImpl hooks;
	private final NPC npc;
	@Override
	public void run() {
		Player npcPlayer = NPCUtils.getAsPlayer(getNpc());
		if (!isOnline()) {
			if (hasNPC()) {
				if (isNPCDieWhenDespawn()) {
					getAsPlayer().setHealth(0);
					updatePlayerData();
				} else {
					despawnNPC();
				}
			}
		}
	}
	
	public void despawnNPC() {
		hooks.despawnNPC(getNpc());
	}
	
	public Player getAsPlayer() {
		return NPCUtils.getAsPlayer(getNpc());
	}
	
	public boolean isOnline() {
		return Bukkit.getOfflinePlayer(getRealPlayer()).isOnline();
	}
	
	public boolean hasNPC() {
		return hooks.getNPC(getRealPlayer()) != null;
	}
	
	public boolean isNPCDieWhenDespawn() {
		return hooks.isNPCDieWhenDespawn();
	}
	
	public void updatePlayerData() {
		NPCUtils.updatePlayerData(getNpc(), getRealPlayer());
	}

}
