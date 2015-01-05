package techcable.minecraft.combattag.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import techcable.minecraft.combattag.event.CombatTagByPlayerEvent;
import techcable.minecraft.combattag.event.CombatTagEvent;

public class CompatibilityListener implements Listener {
	@EventHandler
	public void onCombatTag(CombatTagEvent event) {
		if (!event.getDefender().isAuthenticated()) event.setCancelled(true);
	}
	@EventHandler
	public void onCombatTagByPlayer(CombatTagByPlayerEvent event) {
		if (!event.getCTAttacker().isAuthenticated()) event.setCancelled(true);
	}
}
