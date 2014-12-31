package techcable.minecraft.combattag.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import techcable.minecraft.combattag.Utils;
import techcable.minecraft.combattag.entity.CombatTagPlayer;
import techcable.minecraft.combattag.event.CombatLogEvent;

import lombok.*;

public class InstakillListener implements Listener {
	@EventHandler(priority=EventPriority.MONITOR)
	public void onCombatLog(CombatLogEvent event) {
		CombatTagPlayer player = event.getPlayer();
		player.getPlayer().damage(1000);
		player.untag();
		Utils.debug(player.getName() + " has been instakilled.");
	}
}
