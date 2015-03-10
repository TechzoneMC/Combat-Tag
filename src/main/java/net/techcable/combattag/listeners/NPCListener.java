package net.techcable.combattag.listeners;

import net.techcable.combattag.CombatPlayer;
import net.techcable.combattag.NPCManager;
import net.techcable.combattag.Utils;
import net.techcable.combattag.event.CombatLogEvent;
import net.techcable.npclib.NPC;
import net.techcable.techutils.inventory.InventoryUtils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.trc202.settings.Settings;

import lombok.*;

public class NPCListener implements Listener {
	@EventHandler
	public void onCombatLog(CombatLogEvent event) {
		CombatPlayer player = event.getPlayer();
		NPC npc = NPCManager.getInstance().spawnNPC(player);
		if (getSettings().getNpcDespawnTime() > 0) {
			NPCManager.getInstance().delayedDespawn(npc, getSettings().getNpcDespawnTime() * 20, getSettings().isNpcDieAfterTime());
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		CombatPlayer player = CombatPlayer.getPlayer(event.getPlayer());
		if (!player.hasNPC()) return; //Player didn't combat log
		NPCManager.getInstance().despawn(player.getNpc());
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent event) {
		if (!NPCManager.getInstance().isNPC(event.getEntity())) return;
		NPC npc = NPCManager.getInstance().getNpc(event.getEntity());
		NPCManager.syncInventory(npc);
	}
	
	public static Settings getSettings() {
		return Utils.getPlugin().getSettings();
	}
}
