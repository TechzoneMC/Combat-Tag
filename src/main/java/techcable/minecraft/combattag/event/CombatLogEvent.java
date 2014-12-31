package techcable.minecraft.combattag.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import techcable.minecraft.combattag.entity.CombatTagPlayer;

import lombok.*;

@Getter
@RequiredArgsConstructor
public class CombatLogEvent extends Event implements Cancellable {
	private final CombatTagPlayer player;
	@Setter
	private boolean cancelled;
	@Getter
	private static final HandlerList handlerList = new HandlerList();
	@Override
	public HandlerList getHandlers() {
		return getHandlerList();
	}
}
