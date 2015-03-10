package net.techcable.combattag;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.base.Preconditions;

import net.techcable.npclib.NPC;
import net.techcable.npclib.NPCRegistry;
import net.techcable.techutils.entity.TechPlayer;
import lombok.*;

public class CombatPlayer extends TechPlayer {
	private long tagTime = -1;
	
	public CombatPlayer(UUID player, CombatTag plugin) {
		super(player, plugin);
	}
	
	public CombatTag getPlugin() {
	    return (CombatTag) super.getPlugin();
	}
	
	public boolean isTagged() {
		return tagTime > System.currentTimeMillis();
	}
	
	public void untag() {
		this.tagTime = -1;
	}
	
	public void tag() {
		this.tagTime = System.currentTimeMillis() + (getPlugin().getSettings().getTagDuration() * 1000);
	}
	
	public long getRemainingTagTime() {
		if (!isTagged()) return -1;
		return tagTime - System.currentTimeMillis();
	}
	
	public boolean hasNPC() {
		return getNpc() != null;
	}
	
	public NPC getNpc() {
		return NPCManager.getInstance().getNpc(getId());
	}
	
	public static CombatPlayer getPlayer(UUID id) {
		return Utils.getPlugin().getPlayer(id);
	}
	public static CombatPlayer getPlayer(Player player) {
		return getPlayer(player.getUniqueId());
	}
	
	public boolean isDisguised() {
	    return PluginCompatibility.isDisguised(getEntity());
	}
	
	public void unDisguise() {
	    PluginCompatibility.unDisguise(getEntity());
	}
	
	
	public String getNpcName() {
		String npcName = getPlugin().getSettings().getNpcName();
		if (!npcName.contains("player") && !npcName.contains("number")) {
			return npcName + NPCManager.getInstance().getNpcNumber();
		}
		return npcName.replace("player", getName()).replace("number", Integer.toString(NPCManager.getInstance().getNpcNumber()));
	}
}