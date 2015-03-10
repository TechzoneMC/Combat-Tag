package net.techcable.combattag.listeners;

import com.trc202.settings.Settings;

import net.techcable.combattag.PluginCompatibility;
import net.techcable.combattag.Utils;
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
    
    public Settings getSettings() {
        return Utils.getPlugin().getSettings();
    }
}
