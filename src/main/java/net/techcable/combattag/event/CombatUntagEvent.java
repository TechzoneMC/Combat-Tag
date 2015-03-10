package net.techcable.combattag.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import net.techcable.combattag.CombatPlayer;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
@Getter
public class CombatUntagEvent extends Event {
	private final CombatPlayer player;
	
	@Getter
	private static final HandlerList handlerList = new HandlerList();
	
	@Override
	public HandlerList getHandlers() {
		return getHandlerList();
	}
}
