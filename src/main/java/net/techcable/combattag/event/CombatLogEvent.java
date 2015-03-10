package net.techcable.combattag.event;

import net.techcable.combattag.CombatPlayer;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.*;

@Getter
@RequiredArgsConstructor
public class CombatLogEvent extends Event implements Cancellable {
	private final CombatPlayer player;
	@Setter
	private boolean cancelled;
	@Getter
	private static final HandlerList handlerList = new HandlerList();
	@Override
	public HandlerList getHandlers() {
		return getHandlerList();
	}
}
