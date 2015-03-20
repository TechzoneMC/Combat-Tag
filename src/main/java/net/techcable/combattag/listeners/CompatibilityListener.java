package net.techcable.combattag.listeners;

import net.techcable.combattag.PluginCompatibility;
import net.techcable.combattag.Utils;
import net.techcable.combattag.config.MainConfig;
import net.techcable.combattag.event.CombatTagEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;

public class CompatibilityListener implements Listener {
	@EventHandler
	public void onCombatTag(CombatTagEvent event) {
		if (event.getPlayer().isDisguised() && getSettings().isDisableDisguisesInCombat()) event.getPlayer().unDisguise();
	}
    
    public MainConfig getSettings() {
        return Utils.getPlugin().getConfig();
    }
}
