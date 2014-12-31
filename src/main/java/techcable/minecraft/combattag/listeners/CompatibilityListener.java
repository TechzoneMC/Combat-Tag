package techcable.minecraft.combattag.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import techcable.minecraft.combattag.event.CombatTagEvent;

public class CompatibilityListener implements Listener {
	@EventHandler
	public void onCombatTag(CombatTagEvent event) {
		if (!event.getDefender().isAuthenticated()) event.setCancelled(true);
		if (event.isAttackerPlayer() && !event.attackerAsPlayer().isAuthenticated()) event.setCancelled(true);
	}
}
