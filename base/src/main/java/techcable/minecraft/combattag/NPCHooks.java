package techcable.minecraft.combattag;

import org.bukkit.entity.Entity;

import lombok.*;

@Getter
public class NPCHooks {
	public void onDisable() {}
	public void init() {}
	public int wipeAll() {
		return 0;
	}
	public boolean isNPC(Entity entity) {
		return false;
	}
}
