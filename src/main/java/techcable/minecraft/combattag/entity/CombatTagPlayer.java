package techcable.minecraft.combattag.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.base.Preconditions;
import com.trc202.CombatTag.CombatTag;

import fr.xephi.authme.cache.auth.PlayerCache;

import techcable.minecraft.combattag.CombatTagAPI;
import techcable.minecraft.combattag.PluginCompatibility;
import techcable.minecraft.combattag.Utils;
import techcable.minecraft.techutils.entity.TechPlayer;
import techcable.minecraft.techutils.utils.EasyCache;
import techcable.minecraft.techutils.utils.EasyCache.Loader;

import lombok.*;

@Getter
public class CombatTagPlayer extends TechPlayer {
	private long remainingTagTime;
	public CombatTagPlayer(UUID player, CombatTag plugin) {
		super(player, plugin);
	}
	
	public CombatTag getPlugin() {
	    return (CombatTag) super.getPlugin();
	}
	
	@Setter
	private CombatTagNPC npc;
	
	public boolean isTagged() {
		return CombatTagAPI.isTagged(getUuid());
	}
	
	public void untag() {
		CombatTagAPI.setTagged(getUuid(), false);
	}
	
	public void copyToNPC() {
		Preconditions.checkState(hasNPC(), "Doesn't have npc");
		copyTo(getNpc().getAsPlayer());
	}
	
	public void copyFromNPC() {
		Preconditions.checkState(hasNPC(), "Doesn't have npc");
		copyFrom(getNpc().getAsPlayer());
	}
	
	public void tag() {
		CombatTagAPI.setTagged(getUuid(), true);
	}
	
	public long getTagTime() {
		return CombatTagAPI.getRemainingTagTime(this.getUuid());
	}
	
	public boolean hasNPC() {
		return npc != null;
	}
	
	public static CombatTagPlayer getPlayer(UUID id) {
		return Utils.getPlugin().getPlayer(id);
	}
	public static CombatTagPlayer getPlayer(OfflinePlayer player) {
		return getPlayer(player.getUniqueId());
	}
	
	public void createNPC() {
		CombatTagNPC npc = CombatTagNPC.createNPC(getUuid());
		this.npc = npc;
	}
	
	
	public boolean isAuthenticated() {
		return PluginCompatibility.isAuthenticated(getPlayer()); 
	}
	
	public boolean isDisguised() {
	    return PluginCompatibility.isDisguised(getPlayer());
	}
	
	public void unDisguise() {
	    PluginCompatibility.unDisguise(getPlayer());
	}
}
