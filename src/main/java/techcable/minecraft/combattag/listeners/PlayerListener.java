package techcable.minecraft.combattag.listeners;

import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityListeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.google.common.collect.Sets;
import com.trc202.CombatTag.CombatTag;

import techcable.minecraft.combattag.CombatTagAPI;
import techcable.minecraft.combattag.PluginCompatibility;
import techcable.minecraft.combattag.Utils;
import static techcable.minecraft.combattag.Utils.getPlugin;
import techcable.minecraft.combattag.entity.CombatTagPlayer;
import techcable.minecraft.combattag.event.CombatLogEvent;
import techcable.minecraft.combattag.event.CombatTagByPlayerEvent;
import techcable.minecraft.combattag.event.CombatTagEvent;

public class PlayerListener implements Listener {
    public static final double KNOCKBACK_POWER = 1.5;
    
    /*
     * Highly buggy
     *
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!Utils.getPlugin().settings.isStopCombatSafezoning()) return;
        if (event.getPlayer().hasPermission("combattag.safezone.ignore"));
        if (!Utils.getPlugin().inTagged(event.getPlayer().getUniqueId())) return;
        if (PluginCompatibility.isPvpDisabled(event.getTo())) {
            knockback(event.getPlayer());
            event.getPlayer().sendMessage("[CombatTag] You can't enter a safezone while combat tagged");
        }
    }
    */
    public static void knockback(Player player) {
    	if (getPlugin().getPlayer(player).isOnline()) {
    		getPlugin().getPlayer(player).knockback(KNOCKBACK_POWER);
    	}
    }
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onDamage(EntityDamageByEntityEvent event) {
    	if (!(event.getEntity() instanceof Player)) return; //Not a player
    	CombatTagPlayer defender = getPlugin().getPlayer(((Player)event.getEntity()).getUniqueId());
    	LivingEntity attacker = Utils.getRootDamager(event.getDamager());
    	if (attacker == null) return;
    	if (attacker instanceof Player) {
    		CombatTagByPlayerEvent tagEvent = new CombatTagByPlayerEvent(defender, (Player)attacker);
    		if (tagEvent.getDefender().isTagged() && tagEvent.getCTAttacker().isTagged()) return;
    		Utils.fire(tagEvent);
    		if (tagEvent.isCancelled()) return;
    		if (tagEvent.isTagDefender()) defender.tag();
    		if (tagEvent.isTagAttacker()) tagEvent.getCTAttacker().tag();
    	} else {
    		if (!Utils.getPlugin().settings.mobTag()) return;
    		CombatTagEvent tagEvent = new CombatTagEvent(defender, attacker);
    		if (defender.isTagged()) return;
    		Utils.fire(tagEvent);
    		if (tagEvent.isCancelled()) return;
    		if (tagEvent.isTagDefender()) defender.tag();
    	}
    }
    @EventHandler(priority=EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
    	if (CombatTagAPI.isNPC(event.getPlayer())) return; //Don't combat log npcs
    	CombatTagPlayer player = getPlugin().getPlayer(event.getPlayer().getUniqueId());
    	if (player.isTagged()) {
    		Utils.fire(new CombatLogEvent(player));
    	}
    }
}
