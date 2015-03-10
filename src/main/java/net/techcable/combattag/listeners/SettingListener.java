package net.techcable.combattag.listeners;

import net.techcable.combattag.CombatPlayer;
import net.techcable.combattag.Utils;
import net.techcable.combattag.event.CombatTagEvent;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import com.trc202.settings.Settings;

import static techcable.minecraft.combattag.CombatTagAPI.isNPC;
import lombok.*;
import techcable.minecraft.combattag.CombatTagAPI;

public class SettingListener implements Listener {
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onCombatTagMonitor(CombatTagEvent event) {
		if (event.isBecauseOfAttack()) {
			event.getPlayer().getEntity().sendMessage(getSettings().getTagMessageDamager().replace("[player]", Utils.getName(event.getCause())));
		}
		if (event.isBecauseOfDefend()) {
			event.getPlayer().getEntity().sendMessage(getSettings().getTagMessageDamaged().replace("[player]", Utils.getName(event.getCause())));
		}
	}
	

	@EventHandler(ignoreCancelled = true)
	public void onCombatTag(CombatTagEvent event) {
		if (getSettings().onlyDamagerTagged() && event.isBecauseOfDefend()) {
			event.setCancelled(true);
		}
		if (ArrayUtils.contains(getSettings().getDisallowedWorlds(), event.getPlayer().getEntity().getLocation().getWorld())) {
			event.setCancelled(true); // Block combat tag in disabled worlds
		}
	}
	@EventHandler
	public void onCombatTagByPlayer(CombatTagEvent event) {
            if (getSettings().blockCreativeTagging() && event.getPlayer().getEntity().getGameMode().equals(GameMode.CREATIVE) && event.isBecauseOfAttack()) {
                event.getPlayer().getEntity().sendMessage("[CombatTag] You can't combat tag in creative mode");
                event.setCancelled(true);
            }
	}
	
	public Settings getSettings() {
            return Utils.getPlugin().getSettings();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
	    if (isNPC(event.getPlayer())) return;
            if (CombatTagAPI.isTagged(event.getPlayer()) && !getSettings().isBlockEditWhileTagged()) {
		event.getPlayer().sendMessage("[CombatTag] You can't break blocks in combat");
                event.setCancelled(true);
            }
        }

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
	    if (isNPC(event.getPlayer())) return;
            if (CombatTagAPI.isTagged(event.getPlayer()) && !getSettings().isBlockEditWhileTagged()) {
                event.getPlayer().sendMessage("[CombatTag] You can't break blocks in combat");
                event.setCancelled(true);
            }
	}
        
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
	    if (isNPC(event.getPlayer())) return;
		CombatPlayer player = CombatPlayer.getPlayer(event.getPlayer());
		if (!player.isTagged()) return;
		if (event.getCause().equals(TeleportCause.PLUGIN) || event.getCause().equals(TeleportCause.UNKNOWN) && getSettings().blockTeleport()) {
			event.getPlayer().sendMessage("[CombatTag] You can't teleport in combat");
			event.setCancelled(true);
		} else if (event.getCause().equals(TeleportCause.ENDER_PEARL) && getSettings().blockEnderPearl()) {
			event.getPlayer().sendMessage("[CombatTag] You can't enderpearl in combat");
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onFly(PlayerToggleFlightEvent event) {
	    if (isNPC(event.getPlayer())) return;
		if (!getSettings().blockFly()) return;
		if (CombatTagAPI.isTagged(event.getPlayer())) {
			event.getPlayer().sendMessage("[CombatTag] You can't fly in combat");
			event.setCancelled(true);
		}
	}
	
	
	//Copied from old listeners
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) { 
	    if (isNPC(event.getPlayer())) return;
		CombatPlayer player = CombatPlayer.getPlayer(event.getPlayer());
		if (player.isTagged()) {
			String command = event.getMessage();
			for (String disabledCommand : getSettings().getDisabledCommands()) {
				if (disabledCommand.equalsIgnoreCase("all") && !command.equalsIgnoreCase("/ct") && !command.equalsIgnoreCase("/combattag")) {
					player.getEntity().sendMessage(ChatColor.RED + "[CombatTag] All commands are disabled while in combat");
					event.setCancelled(true);
					return;
				}
				if (command.indexOf(" ") == disabledCommand.length()) {
					if (command.substring(0, command.indexOf(" ")).equalsIgnoreCase(disabledCommand)) {
						Utils.debug("Combat Tag has blocked the command: " + disabledCommand + " .");
						player.getEntity().sendMessage(ChatColor.RED + "[CombatTagReloaded] This command is disabled while in combat");
						event.setCancelled(true);
						return;
					}
				} else if (disabledCommand.indexOf(" ") > 0) {
					if (command.toLowerCase().startsWith(disabledCommand.toLowerCase())) {
						Utils.debug("Combat Tag has blocked the command: " + disabledCommand + " .");
						player.getEntity().sendMessage(ChatColor.RED + "[CombatTagReloaded] This command is disabled while in combat");
						event.setCancelled(true);
						return;
					}
				} else if (!command.contains(" ") && command.equalsIgnoreCase(disabledCommand)) {
					Utils.debug("Combat Tag has blocked the command: " + disabledCommand + " .");
					player.getEntity().sendMessage(ChatColor.RED + "[CombatTagReloaded] This command is disabled while in combat");
					event.setCancelled(true);
					return;
				}
			}
		}
	}
}
