package techcable.minecraft.combattag.event;

import org.bukkit.entity.Player;

import techcable.minecraft.combattag.entity.CombatTagPlayer;

import lombok.*;

@Getter
public class CombatTagByPlayerEvent extends CombatTagEvent {
	@Setter
	private boolean tagAttacker = true;
	public CombatTagByPlayerEvent(CombatTagPlayer defender, Player attacker) {
		super(defender, attacker);
	}
	
	@Override
	public Player getAttacker() {
		return (Player) super.getAttacker();
	}
	
	public CombatTagPlayer getCTAttacker() {
		return CombatTagPlayer.getPlayer(getAttacker());
	}
}
