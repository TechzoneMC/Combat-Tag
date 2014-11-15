package techcable.minecraft.combattag.instakill;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.trc202.CombatTag.CombatTag;

import techcable.minecraft.combattag.events.PvPLogEvent;

import lombok.*;

@Getter
@RequiredArgsConstructor
public class InstakillListener implements Listener {
	private final InstakillHooksImpl hooks;
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPvpLog(PvPLogEvent pvpLog) {
		if (pvpLog.isCancelled()) return;
		if (hooks.isInstakillEnabled()) return;
		Player logger = pvpLog.getPlayer();
        if (hooks.isDebugEnabled()) {
            hooks.getLog().info("[CombatTag] " + logger.getName() + " has been instakilled!");
        }
        logger.damage(1000L);
        hooks.removeTagged(logger);
	}
}
