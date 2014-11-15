package techcable.minecraft.combattag;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import techcable.minecraft.combattag.events.PvPLogEvent;

import fr.xephi.authme.api.API;

import lombok.*;

@Getter
public class PluginCompatibility {
	private PluginCompatibility() {}
	
	public static boolean isAuthenticated(Player player) {
		if (!hasAuthme()) return true;
		return API.isAuthenticated(player);
	}
	
	public static boolean hasAuthme() {
		try {
			Class.forName("fr.xephi.authme.AuthMe");
		} catch (ClassNotFoundException ex) {
			return false;
		}
		return true;
	}
	
	public static void registerListeners() {
		CompatibilityListener compatListener = new CompatibilityListener();
		Bukkit.getPluginManager().registerEvents(compatListener, Utils.getPlugin());
	}
	
	public static class CompatibilityListener implements Listener {
		@EventHandler(priority=EventPriority.HIGHEST)
		public void onPvpLog(PvPLogEvent event) {
			if (!isAuthenticated(event.getPlayer())) event.setCancelled(true);
		}
	}
}
