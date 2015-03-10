package net.techcable.combattag;

import java.util.logging.Logger;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import lombok.*;

@Getter
public class Utils {
    public static final String PLUGIN_NAME = "CombatTagReloaded";
    public static CombatTag getPlugin() {
        Plugin rawPlugin = Bukkit.getPluginManager().getPlugin(PLUGIN_NAME);
        if (rawPlugin instanceof CombatTag) {
            return (CombatTag) rawPlugin;
        } else {
            return null;
        }
    }
    public static boolean isDebug() {
    	return getPlugin().getSettings().isDebugEnabled();
    }
    public static Logger getLogger() {
    	return Bukkit.getLogger();
    }
    public static void info(String s) {
    	getLogger().info("[CombatTagReloaded] " + s);
    }
    public static void debug(String s) {
    	if (isDebug()) {
    		info(s);
    	}
    }
    public static void warning(String s) {
    	getLogger().warning("[CombatTagReloaded] " + s);
    }
    public static void severe(String s) {
    	getLogger().severe("[CombatTagReloaded] " + s);
    }
    public static void fire(Event event) {
    	PluginManager manager = Bukkit.getPluginManager();
    	manager.callEvent(event);
    }
    public static LivingEntity getRootDamager(Entity damager) {
    	if (damager instanceof Projectile) {
    		Projectile projectile = (Projectile) damager;
    		ProjectileSource source = projectile.getShooter();
    		if (source instanceof Entity) {
    			return getRootDamager((Entity)source);
    		} else {
    			return null;
    		}
    	} else if (damager instanceof Tameable) {
    		Tameable pet = (Tameable) damager;
    		AnimalTamer owner = pet.getOwner();
    		if (owner instanceof HumanEntity) {
    			return getRootDamager(damager);
    		} else {
    			return null;
    		}
    	} else if (damager instanceof LivingEntity) {
    		return (LivingEntity) damager;
    	} else {
    		return null;
    	}
    }
    public static String getName(LivingEntity entity) {
    	if (entity instanceof HumanEntity) {
    		return ((HumanEntity)entity).getName();
    	} else {
    		if (entity.getCustomName() != null) {
    			return entity.getCustomName();
    		} else {
    			return WordUtils.capitalizeFully(entity.getType().toString().replace("_", " "));
    		}
    	}
    }
    public static boolean isVisible(Player player, LivingEntity entity) {
   		if (entity.hasPotionEffect(PotionEffectType.INVISIBILITY)) return false;
    	if (entity instanceof Player) {
   			return player.canSee(((Player)entity));
    	}
    	return true;
    }
}
