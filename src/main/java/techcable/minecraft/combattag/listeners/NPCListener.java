package techcable.minecraft.combattag.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.trc202.settings.Settings;

import techcable.minecraft.combattag.Utils;
import techcable.minecraft.combattag.entity.CombatTagNPC;
import techcable.minecraft.combattag.entity.CombatTagPlayer;
import techcable.minecraft.combattag.event.CombatLogEvent;

import lombok.*;

public class NPCListener implements Listener {
	@EventHandler
	public void onCombatLog(CombatLogEvent event) {
		CombatTagPlayer player = event.getPlayer();
		player.createNPC();
		player.copyToNPC();
		if (getSettings().getNpcDespawnTime() > 0) {
			player.getNpc().scheduleDelayedDespawn(getSettings().getNpcDespawnTime() * 1000, getSettings().isNpcDieAfterTime());
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		CombatTagPlayer player = CombatTagPlayer.getPlayer(event.getPlayer());
		if (!player.hasNPC()) return; //Player didn't combat log
		player.getNpc().syncInventory(); //Get the npcs stuff
		player.getNpc().despawn();
	}
	
	public static Settings getSettings() {
		return Utils.getPlugin().settings;
	}
}
