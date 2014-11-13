package techcable.minecraft.combattag.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.*;

@Getter
@RequiredArgsConstructor
public class PlayerTagEvent extends Event implements Cancellable {
    private final Player tagged;
    private final Entity damager;
    @Setter
    private boolean cancelled;

    private HandlerList handlerList;

    @Override
    public HandlerList getHandlers() {
	return getHandlerList();
    }
}