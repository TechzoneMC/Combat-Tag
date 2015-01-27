package techcable.minecraft.combattag.listeners;

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

import com.sk89q.worldguard.blacklist.event.BlockPlaceBlacklistEvent;
import com.trc202.settings.Settings;

import static techcable.minecraft.combattag.CombatTagAPI.isNPC;
import techcable.minecraft.combattag.Utils;
import techcable.minecraft.combattag.entity.CombatTagPlayer;
import techcable.minecraft.combattag.event.CombatTagByPlayerEvent;
import techcable.minecraft.combattag.event.CombatTagEvent;

import lombok.*;

public class SettingListener implements Listener {
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onCombatTagMonitor(CombatTagEvent event) {
		event.getDefender().getPlayer().sendMessage(getSettings().getTagMessageDamaged().replace("[player]", Utils.getName(event.getAttacker())));
		if (getSettings().blockFly() && event.getDefender().getPlayer().isFlying()) {
			event.getDefender().getPlayer().setFlying(false);
		}
	}
	
	public void onCombatTagByPlayerMonitor(CombatTagByPlayerEvent event) {
		if (getSettings().isSendMessageWhenTagged()) {
			event.getAttacker().sendMessage(getSettings().getTagMessageDamager().replace("[player]", event.getDefender().getName()));
		}
	}
	

	@EventHandler(ignoreCancelled = true)
	public void onCombatTag(CombatTagEvent event) {
		if (getSettings().onlyDamagerTagged()) {
			event.setTagDefender(false);
		}
		if (ArrayUtils.contains(getSettings().getDisallowedWorlds(), event.getDefender().getLocation().getWorld())) {
			event.setCancelled(true); // Block combat tag in disabled worlds
		}
	}
	@EventHandler
	public void onCombatTagByPlayer(CombatTagByPlayerEvent event) {
		if (getSettings().blockCreativeTagging() && event.getAttacker().getGameMode().equals(GameMode.CREATIVE)) {
			event.setCancelled(true);
		}
	}
	
	public Settings getSettings() {
		return Utils.getPlugin().settings;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
	    if (isNPC(event.getPlayer())) return;
		CombatTagPlayer player = CombatTagPlayer.getPlayer(event.getPlayer());
		if (player.isTagged() && getSettings().isBlockEditWhileTagged()) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
	    if (isNPC(event.getPlayer())) return;
		CombatTagPlayer player = CombatTagPlayer.getPlayer(event.getPlayer());
		if (player.isTagged() && getSettings().isBlockEditWhileTagged()) {
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
	    if (isNPC(event.getPlayer())) return;
		CombatTagPlayer player = CombatTagPlayer.getPlayer(event.getPlayer());
		if (!player.isTagged()) return;
		if (event.getCause().equals(TeleportCause.PLUGIN) || event.getCause().equals(TeleportCause.UNKNOWN) && getSettings().blockTeleport()) {
			event.setCancelled(true);
		} else if (event.getCause().equals(TeleportCause.ENDER_PEARL) && getSettings().blockEnderPearl()) {
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onFly(PlayerToggleFlightEvent event) {
	    if (isNPC(event.getPlayer())) return;
		if (!getSettings().blockFly()) return;
		CombatTagPlayer player = CombatTagPlayer.getPlayer(event.getPlayer());
		if (player.isTagged()) event.setCancelled(true);
	}
	
	
	//Copied from old listeners
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) { 
	    if (isNPC(event.getPlayer())) return;
		CombatTagPlayer player = CombatTagPlayer.getPlayer(event.getPlayer());
		if (player.isTagged()) {
			String command = event.getMessage();
			for (String disabledCommand : getSettings().getDisabledCommands()) {
				if (disabledCommand.equalsIgnoreCase("all") && !command.equalsIgnoreCase("/ct") && !command.equalsIgnoreCase("/combattag")) {
					player.getPlayer().sendMessage(ChatColor.RED + "[CombatTag] All commands are disabled while in combat");
					event.setCancelled(true);
					return;
				}
				if (command.indexOf(" ") == disabledCommand.length()) {
					if (command.substring(0, command.indexOf(" ")).equalsIgnoreCase(disabledCommand)) {
						Utils.debug("Combat Tag has blocked the command: " + disabledCommand + " .");
						player.getPlayer().sendMessage(ChatColor.RED + "[CombatTagReloaded] This command is disabled while in combat");
						event.setCancelled(true);
						return;
					}
				} else if (disabledCommand.indexOf(" ") > 0) {
					if (command.toLowerCase().startsWith(disabledCommand.toLowerCase())) {
						Utils.debug("Combat Tag has blocked the command: " + disabledCommand + " .");
						player.getPlayer().sendMessage(ChatColor.RED + "[CombatTagReloaded] This command is disabled while in combat");
						event.setCancelled(true);
						return;
					}
				} else if (!command.contains(" ") && command.equalsIgnoreCase(disabledCommand)) {
					Utils.debug("Combat Tag has blocked the command: " + disabledCommand + " .");
					player.getPlayer().sendMessage(ChatColor.RED + "[CombatTagReloaded] This command is disabled while in combat");
					event.setCancelled(true);
					return;
				}
			}
		}
	}
}
