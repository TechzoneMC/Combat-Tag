package techcable.minecraft.combattag;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import lombok.*;
import net.techcable.combattag.CombatPlayer;
import net.techcable.combattag.CombatTag;
import net.techcable.combattag.NPCManager;
import net.techcable.combattag.Utils;

/**
 * API to Interface with Combat Tag Reloaded
 * 
 * All methods are static
 * 
 * @author Techcable
 *
 */
public class CombatTagAPI {
	private CombatTagAPI() {}
	
	/**
	 * Returns if a player is combat tagged
	 * @param player the player to check
	 * @return true if combat tagged
	 */
	public static boolean isTagged(Player player) {
		return isTagged(player.getUniqueId());
	}
	
	/**
	 * Returns if a player is combat tagged
	 * @param player the player to check
	 * @return true if combat tagged
	 */
	public static boolean isTagged(UUID player) {
		return CombatPlayer.getPlayer(player).isTagged();
	}
	
	/**
	 * Returns the time a player has left in combat
	 * 
	 * Returns -1 if the player isn't in combat
	 * 
	 * @param player the player to check
	 * @return time in milliseconds until the player is no longer in combat
	 */
	public static long getRemainingTagTime(Player player) {
		return getRemainingTagTime(player.getUniqueId());
	}
	
	/**
	 * Returns the time a player has left in combat
	 * 
	 * Returns -1 if the player isn't in combat
	 * 
	 * @param player the player to check
	 * @return time in milliseconds until the player is no longer in combat
	 */
	public static long getRemainingTagTime(UUID player) {
		return CombatPlayer.getPlayer(player).getRemainingTagTime();
	}
	
	/**
	 * Checks if an entity is a NPC
	 * @param entity the entity to check
	 * @return true if entity is a NPC
	 */
	public static boolean isNPC(Entity entity) {
		return NPCManager.getInstance().isNPC(entity);
	}
	
	/**
	 * Set if a player is tagged
	 * @param player the player to set the tag status of
	 * @param tagged true if player should be tagged, false if he should be untagged
	 */
	public static void setTagged(Player player, boolean tagged) {
		setTagged(player.getUniqueId(), tagged);
	}
	/**
	 * Set if a player is tagged
	 * doesn't support tagging or untagging an offline player
	 * 
	 * @param player the player to set the tag status off
	 * @param tagged true if player should be tagged, false if he should be untagged
	 */
	public static void setTagged(UUID player, boolean tagged) {
		if (tagged) {
			CombatPlayer.getPlayer(player).tag();
		} else {
			CombatPlayer.getPlayer(player).untag();
		}
	}
	
	/**
	 * Tag this player
	 * Equivelent to setTagged(player, true)
	 * @param player player to tag
	 */
	public static void addTagged(Player player) {
		setTagged(player, true);
	}
	
	/**
	 * Untag this player
	 * Equivelent to setTagged(player, false)
	 * @param player player to un-tag
	 */
	public static void removeTagged(Player player) {
		setTagged(player, false);
	}
	
	public static CombatTag getPlugin() {
		return Utils.getPlugin();
	}
}
