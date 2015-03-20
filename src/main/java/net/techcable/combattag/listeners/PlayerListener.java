package net.techcable.combattag.listeners;

import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityListeners;

import net.techcable.combattag.CombatPlayer;
import techcable.minecraft.combattag.CombatTagAPI;
import net.techcable.combattag.PluginCompatibility;
import net.techcable.combattag.Utils;
import net.techcable.combattag.event.CombatLogEvent;
import net.techcable.combattag.event.CombatTagEvent;
import net.techcable.combattag.event.CombatTagEvent.TagCause;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.google.common.collect.Sets;

import static net.techcable.combattag.Utils.getPlugin;

public class PlayerListener implements Listener {
    public static final double KNOCKBACK_POWER = 1.5;
    
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!Utils.getPlugin().getSettings().isStopCombatSafezoning()) return;
        if (event.getPlayer().hasPermission("combattag.safezonce.ignore")) return;
        if (!CombatPlayer.getPlayer(event.getPlayer()).isTagged()) return;
        if (PluginCompatibility.isPvpDisabled(event.getTo())) {
            knockback(event.getPlayer());
            event.getPlayer().sendMessage("[CombatTag] You can't enter a safezone while combat tagged");
        }
    }
    
    public static void knockback(Player player) {
    	player.setVelocity(player.getVelocity().multiply(-KNOCKBACK_POWER).setY(0.5));
    }
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onDamage(EntityDamageByEntityEvent event) {
    	LivingEntity attacker = Utils.getRootDamager(event.getDamager());
    	LivingEntity defender = (LivingEntity) event.getEntity();
    	if (CombatTagAPI.isNPC(defender) || CombatTagAPI.isNPC(attacker)) return;
    	if (attacker instanceof Player) onAttack(CombatPlayer.getPlayer((Player)attacker), defender);
    	if (defender instanceof Player) onDefend(CombatPlayer.getPlayer((Player)defender), attacker);
    }
    
    public void onAttack(CombatPlayer attacker, LivingEntity defender) {
    	if (!(defender instanceof Player) && !Utils.getPlugin().getSettings().isMobTag()) return;
    	if (attacker.isTagged()) return;
    	CombatTagEvent event = new CombatTagEvent(attacker, defender, TagCause.ATTACK);
    	Utils.fire(event);
    	if (event.isCancelled()) return;
    	attacker.tag();
    }
    
    public void onDefend(CombatPlayer defender, LivingEntity attacker) {
    	if (!(attacker instanceof Player) && !Utils.getPlugin().getSettings().isMobTag()) return;
    	if (defender.isTagged()) return;
    	CombatTagEvent event = new CombatTagEvent(defender, attacker, TagCause.DEFEND);
    	Utils.fire(event);
    	if (event.isCancelled()) return;
    	defender.tag();
    }
    
    @EventHandler(priority=EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
    	if (CombatTagAPI.isNPC(event.getPlayer())) return; //Don't combat log npcs
    	CombatPlayer player = getPlugin().getPlayer(event.getPlayer().getUniqueId());
    	if (player.isTagged()) {
    		Utils.fire(new CombatLogEvent(player));
    	}
    }
    
    @EventHandler(priority=EventPriority.MONITOR)
    public void onDeath(PlayerDeathEvent event) {
    	if (CombatTagAPI.isNPC(event.getEntity())) return;
    	CombatPlayer player = CombatPlayer.getPlayer(event.getEntity());
    	player.untag();
    }
}
