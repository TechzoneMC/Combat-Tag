package techcable.minecraft.combattag.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import lombok.*;

@RequiredArgsConstructor
@Getter
public class PvPLogEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    
    @Override
    public HandlerList getHandlers() {
	return getHandlerList();
    }
 
    public static HandlerList getHandlerList() {
	return handlers;
    }

    private final Player player;
    @Setter
    private boolean cancelled;
}