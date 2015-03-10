package net.techcable.combattag.listeners;

import net.techcable.combattag.event.CombatLogEvent;
import net.techcable.combattag.event.CombatTagEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PermissionListener implements Listener {
	public static String DONT_COMBAT_LOG_PERM = "combattag.ignore.pvplog";
	public static String DONT_COMBAT_TAG_PERM = "combattag.ignore";
	@EventHandler
	public void onCombatLog(CombatLogEvent event) {
		if (event.getPlayer().getEntity().hasPermission(DONT_COMBAT_LOG_PERM)) {
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onCombatTag(CombatTagEvent event) {
		if (event.getPlayer().getEntity().hasPermission(DONT_COMBAT_TAG_PERM)) {
			event.setCancelled(true);
		}
	}
}
