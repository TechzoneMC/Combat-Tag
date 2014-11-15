package techcable.minecraft.combattag.instakill;

import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.trc202.CombatTag.CombatTag;

import techcable.minecraft.combattag.InstakillHooks;

import lombok.*;

@Getter
@RequiredArgsConstructor
public class InstakillHooksImpl extends InstakillHooks {
	private CombatTag plugin;
	
	public void init() {
		InstakillListener listener = new InstakillListener(this);
		Bukkit.getPluginManager().registerEvents(listener, getPlugin());
	}
	
	public boolean isInstakillEnabled() {
		return getPlugin().settings.isInstaKill();
	}
	
	public void removeTagged(Player player) {
		getPlugin().removeTagged(player.getUniqueId());
	}
	
	public boolean isDebugEnabled() {
		return getPlugin().isDebugEnabled();
	}
	
	public Logger getLog() {
		return getPlugin().log;
	}
}
