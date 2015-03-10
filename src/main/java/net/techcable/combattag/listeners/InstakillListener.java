package net.techcable.combattag.listeners;

import net.techcable.combattag.CombatPlayer;
import net.techcable.combattag.Utils;
import net.techcable.combattag.event.CombatLogEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import lombok.*;

public class InstakillListener implements Listener {
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onCombatLog(CombatLogEvent event) {
		CombatPlayer player = event.getPlayer();
		player.getEntity().damage(1000);
		player.untag();
		Utils.debug(player.getName() + " has been instakilled.");
	}
}
