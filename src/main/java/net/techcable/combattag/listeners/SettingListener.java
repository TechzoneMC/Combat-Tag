package net.techcable.combattag.listeners;

import com.trc202.settings.Settings;
import lombok.*;
import lombok.core.Main;
import net.techcable.combattag.CombatPlayer;
import net.techcable.combattag.Utils;
import net.techcable.combattag.config.MainConfig;
import net.techcable.combattag.config.MessageConfig;
import net.techcable.combattag.event.CombatTagEvent;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import techcable.minecraft.combattag.CombatTagAPI;

import javax.rmi.CORBA.Util;

import static techcable.minecraft.combattag.CombatTagAPI.isNPC;

public class SettingListener implements Listener {
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onCombatTagMonitor(CombatTagEvent event) {
		if (event.isBecauseOfAttack()) {
			event.getPlayer().getEntity().sendMessage(getMessages().getTagMessageDamager(Utils.getName(event.getCause())));
		}
		if (event.isBecauseOfDefend()) {
			event.getPlayer().getEntity().sendMessage(getMessages().getTagMessageDamaged(Utils.getName(event.getCause())));
		}
	}
	

	@EventHandler(ignoreCancelled = true)
	public void onCombatTag(CombatTagEvent event) {
		if (getSettings().isOnlyDamagerTagged() && event.isBecauseOfDefend()) {
			event.setCancelled(true);
		}
		if (isDisabledWorld(event.getPlayer().getEntity().getLocation().getWorld())) {
			event.setCancelled(true); // Block combat tag in disabled worlds
		}
	}
	@EventHandler
	public void onCombatTagByPlayer(CombatTagEvent event) {
            if (getSettings().isBlockCreativeTagging() && event.getPlayer().getEntity().getGameMode().equals(GameMode.CREATIVE) && event.isBecauseOfAttack()) {
                event.getPlayer().getEntity().sendMessage(getMessages().getCantTagInCreative());
                event.setCancelled(true);
            }
	}
	
	public MainConfig getSettings() {
            return Utils.getPlugin().getSettings();
	}

    public MessageConfig getMessages() {
        return Utils.getPlugin().getMessages();
    }

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
	    if (isNPC(event.getPlayer())) return;
            if (CombatTagAPI.isTagged(event.getPlayer()) && !getSettings().isAllowBlockEditInCombat()) {
		        event.getPlayer().sendMessage(getMessages().getCantPlaceBlocksInCombat());
                event.setCancelled(true);
            }
        }

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
	    if (isNPC(event.getPlayer())) return;
            if (CombatTagAPI.isTagged(event.getPlayer()) && !getSettings().isAllowBlockEditInCombat()) {
                event.getPlayer().sendMessage(getMessages().getCantBreakBlocksInCombat());
                event.setCancelled(true);
            }
	}
        
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
	    if (isNPC(event.getPlayer())) return;
		CombatPlayer player = CombatPlayer.getPlayer(event.getPlayer());
		if (!player.isTagged()) return;
		if (event.getCause().equals(TeleportCause.PLUGIN) || event.getCause().equals(TeleportCause.UNKNOWN) && getSettings().isBlockTeleportInCombat()) {
			event.getPlayer().sendMessage(getMessages().getCantTeleportInCombat());
			event.setCancelled(true);
		} else if (event.getCause().equals(TeleportCause.ENDER_PEARL) && getSettings().isBlockEnderPearlInCombat()) {
			event.getPlayer().sendMessage(getMessages().getCantEnderpearlInCombat());
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onFly(PlayerToggleFlightEvent event) {
	    if (isNPC(event.getPlayer())) return;
		if (!getSettings().isBlockFlyInCombat()) return;
		if (CombatTagAPI.isTagged(event.getPlayer())) {
			event.getPlayer().sendMessage(getMessages().getCantFlyInCombat());
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
					player.getEntity().sendMessage(getMessages().getAllCommandsDisabled());
					event.setCancelled(true);
					return;
				}
				if (command.indexOf(" ") == disabledCommand.length()) {
					if (command.substring(0, command.indexOf(" ")).equalsIgnoreCase(disabledCommand)) {
						Utils.debug("Combat Tag has blocked the command: " + disabledCommand + " .");
						player.getEntity().sendMessage(getMessages().getThisCommandDisabled());
						event.setCancelled(true);
						return;
					}
				} else if (disabledCommand.indexOf(" ") > 0) {
					if (command.toLowerCase().startsWith(disabledCommand.toLowerCase())) {
						Utils.debug("Combat Tag has blocked the command: " + disabledCommand + " .");
						player.getEntity().sendMessage(getMessages().getThisCommandDisabled());
						event.setCancelled(true);
						return;
					}
				} else if (!command.contains(" ") && command.equalsIgnoreCase(disabledCommand)) {
					Utils.debug("Combat Tag has blocked the command: " + disabledCommand + " .");
					player.getEntity().sendMessage(getMessages().getThisCommandDisabled());
					event.setCancelled(true);
					return;
				}
			}
		}
	}
        
    private boolean isDisabledWorld(World world) {
        for (String wName : getSettings().getDisallowedWorlds()) {
            if (wName.equalsIgnoreCase(world.getName())) return true;
        }
        return false;
    }
}
