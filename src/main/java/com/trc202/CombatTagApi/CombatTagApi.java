package com.trc202.CombatTagApi;

import net.techcable.combattag.CombatTagAPI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.omg.CosNaming.IstringHelper;

/**
 * @deprecated use techcable.minecraft.combattag CombatTagAPI
 */
@Deprecated
public class CombatTagApi {
	
	/**
	 * 
	 * @deprecated use no-args construcor
	 * @param plugin ignored
	 */
	@Deprecated
    public CombatTagApi(Object plugin) {}

    /**
     * Checks to see if the player is in combat. The combat time can be
     * configured by the server owner If the player has died while in combat the
     * player is no longer considered in combat and as such will return false
     *
     * @param player
     * @return true if player is in combat
     */
    @Deprecated
    public boolean isInCombat(Player player) {
        return CombatTagAPI.isTagged(player.getUniqueId());
    }

    /**
     * Checks to see if the player is in combat. The combat time can be
     * configured by the server owner If the player has died while in combat the
     * player is no longer considered in combat and as such will return false
     *
     * @param name
     * @return true if player is online and in combat
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public boolean isInCombat(String name) {
        Player player = Bukkit.getPlayerExact(name);
        if (player != null) {
            return CombatTagAPI.isTagged(player);
        }
        return false;
    }

    /**
     * Returns the time before the tag is over -1 if the tag has expired -2 if
     * the player is not in combat
     *
     * @param player
     * @return
     */
    
    @Deprecated
    public long getRemainingTagTime(Player player) {
        if (isInCombat(player)) {
            return CombatTagAPI.getRemainingTagTime(player);
        } else {
            return -1L;
        }
    }

    /**
     * Returns the time before the tag is over -1 if the tag has expired -2 if
     * the player is not in combat
     *
     * @param name
     * @return
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public long getRemainingTagTime(String name) {
        if (Bukkit.getPlayerExact(name) != null) {
            Player player = Bukkit.getPlayerExact(name);
            return getRemainingTagTime(player);
        }
        return -2L;
    }

    /**
     * Returns if the entity is an NPC
     *
     * @param entity
     * @return true if the player is an NPC
     */
    @Deprecated
    public boolean isNPC(Entity entity) {
    	return CombatTagAPI.isNPC(entity);
    }

    /**
     * Tags player
     *
     * @param player
     * @return true if the action is successful, false if not
     */
    @Deprecated
    public boolean tagPlayer(Player player) {
        if (isInCombat(player)) return false;
    	CombatTagAPI.addTagged(player);
    	return true;
    }

    /**
     * Untags player
     *
     * @param player
     */
    @Deprecated
    public void untagPlayer(Player player) {
        CombatTagAPI.removeTagged(player);
    }

    /**
     * Returns the value of a configuration option with the specified name
     *
     * @param configKey
     * @return String value of option
     */
    @Deprecated
    public String getConfigOption(String configKey) {
        return CombatTagAPI.getPlugin().getSettingsHelper().getProperty(configKey);
    }
}
