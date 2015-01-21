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
	@EventHandler(ignoreCancelled=true)
     	public void onDamage(EntityDamageByEntityEvent event) {
     		if ((event.getEntity() instanceof Player) && !event.getEntity().isAuthenticated()) {
            		event.setCancelled(true);
			return;
        	}
        	if ((Utils.getRootDamager(event.getDamager()) != null) && 
        		(Utils.getRootDamager(event.getDamager()) instanceof Player) && 
        		!PluginCompatibility.isAuthenticated((Player)Utils.getRootDamager(event.getDamager()))) {
            		event.setCancelled(true);
            		return;
        	}
     	}
}
