package techcable.minecraft.combattag.instakill;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.trc202.CombatTag.CombatTag;

import techcable.minecraft.combattag.events.PvPLogEvent;

import lombok.*;

@Getter
@RequiredArgsConstructor
public class InstakillListener implements Listener {
	private final InstakillHooksImpl hooks;
	
	public void onPvpLog(PvPLogEvent pvpLog) {
		if (hooks.isInstakillEnabled()) return;
		Player logger = pvpLog.getPlayer();
        if (hooks.isDebugEnabled()) {
            hooks.getLog().info("[CombatTag] " + logger.getName() + " has been instakilled!");
        }
        logger.damage(1000L);
        hooks.removeTagged(logger);
	}
}
