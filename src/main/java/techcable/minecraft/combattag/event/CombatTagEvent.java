package techcable.minecraft.combattag.event;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import techcable.minecraft.combattag.entity.CombatTagPlayer;

import lombok.*;

@Getter
@RequiredArgsConstructor
public class CombatTagEvent extends Event implements Cancellable {
	private final CombatTagPlayer defender;
	private final LivingEntity attacker;
	@Setter
	private boolean tagDefender = true;
	@Getter
	private static final HandlerList handlerList = new HandlerList();
	@Setter
	private boolean cancelled;
	@Override
	public HandlerList getHandlers() {
		return getHandlerList();
	}
}
