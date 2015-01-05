package techcable.minecraft.combattag.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import techcable.minecraft.combattag.event.CombatLogEvent;
import techcable.minecraft.combattag.event.CombatTagByPlayerEvent;
import techcable.minecraft.combattag.event.CombatTagEvent;

public class PermissionListener implements Listener {
	public static String DONT_COMBAT_LOG_PERM = "combattag.ignore.pvplog";
	public static String DONT_COMBAT_TAG_PERM = "combattag.ignore";
	@EventHandler
	public void onCombatLog(CombatLogEvent event) {
		if (event.getPlayer().getPlayer().hasPermission(DONT_COMBAT_LOG_PERM)) {
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onCombatTag(CombatTagEvent event) {
		if (event.getDefender().getPlayer().hasPermission(DONT_COMBAT_TAG_PERM)) {
			event.setTagDefender(false);
		}
	}
	@EventHandler
	public void onCombatTagByPlayer(CombatTagByPlayerEvent event) {
		if (event.getAttacker().hasPermission(DONT_COMBAT_TAG_PERM)) {
			event.setTagAttacker(false);
		}
	}
}
