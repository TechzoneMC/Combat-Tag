package techcable.minecraft.combattag.listeners;

import com.trc202.settings.Settings;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;

import techcable.minecraft.combattag.event.CombatTagByPlayerEvent;
import techcable.minecraft.combattag.event.CombatTagEvent;
import techcable.minecraft.combattag.Utils;
import techcable.minecraft.combattag.PluginCompatibility;

public class CompatibilityListener implements Listener {
	@EventHandler
	public void onCombatTag(CombatTagEvent event) {
		if (!event.getDefender().isAuthenticated()) event.setCancelled(true);
		if (event.getDefender().isDisguised() && getSettings().isDisableDisguisesInCombat()) event.getDefender().unDisguise();
	}
	@EventHandler
	public void onCombatTagByPlayer(CombatTagByPlayerEvent event) {
		if (!event.getCTAttacker().isAuthenticated()) event.setCancelled(true);
		if (event.getCTAttacker().isDisguised() && getSettings().isDisableDisguisesInCombat()) event.getCTAttacker().unDisguise();
	}
	@EventHandler(ignoreCancelled=true)
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity defender = event.getEntity();
        Entity attacker = Utils.getRootDamager(event.getDamager());
     	if (defender instanceof Player && !PluginCompatibility.isAuthenticated((Player)defender)) {
        	event.setCancelled(true);
		    return;
        }
        if (attacker != null && attacker instanceof Player && PluginCompatibility.isAuthenticated((Player)attacker)) {
            event.setCancelled(true);
            return;
        }
    }
    
    public Settings getSettings() {
        return Utils.getPlugin().settings;
    }
}
