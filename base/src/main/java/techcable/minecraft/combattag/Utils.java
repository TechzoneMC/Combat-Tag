package techcable.minecraft.combattag;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.trc202.CombatTag.CombatTag;

import techcable.minecraft.offlineplayers.AdvancedOfflinePlayer;

import lombok.*;

@Getter
public class Utils {
	public static void copyPlayer(Player target, Player source) {
        target.getInventory().setContents(source.getInventory().getContents());
        target.getInventory().setArmorContents(source.getInventory().getArmorContents());
        target.setExp(source.getExp());
        target.setLevel(source.getLevel());
        target.setFoodLevel(source.getFoodLevel());
        target.addPotionEffects(source.getActivePotionEffects());
        target.setRemainingAir(source.getRemainingAir());
        target.setExhaustion(source.getExhaustion());
        target.setSaturation(source.getSaturation());
        target.setFireTicks(source.getFireTicks());
        target.setHealth(source.getHealth());
	}
	
	public static void copyPlayer(AdvancedOfflinePlayer target, Player source) {
		target.setItems(source.getInventory().getContents());
		target.setArmor(source.getInventory().getArmorContents());
		target.setExp(source.getExp());
		target.setLevel(source.getLevel());
		target.setFoodLevel(source.getFoodLevel());
		target.addPotionEffects(source.getActivePotionEffects());
		target.setAir(source.getRemainingAir());
		target.setExhaustion(source.getExhaustion());
		target.setSaturation(source.getSaturation());
		target.setFireTicks(source.getFireTicks());
		target.setHealth((float)source.getHealth());
	}

	public static final ItemStack EMPTY = new ItemStack(Material.AIR);

	public static void emptyInventory(AdvancedOfflinePlayer target) {
		ItemStack[] items = target.getItems();
		for (int i = 0; i < items.length; i++) {
			items[i] = EMPTY;
		}
		target.setItems(items);
		ItemStack[] armor = target.getArmor();
		for (int i = 0; i > armor.length; i++) {
			armor[i] = EMPTY;
		}
		target.setArmor(armor);
	}

    public static void fireEvent(Event event) {
    	Bukkit.getServer().getPluginManager().callEvent(event);
    }

    private static NPCHooks hooks;
    private final static String HOOKS_CLASS = "techcable.minecraft.combattag.npc";
	public static NPCHooks getNPCHooks() {
		if (hooks == null) {
			try {
				Class<?> clazz = Class.forName(HOOKS_CLASS);
				Constructor<?> constructor = clazz.getConstructor(CombatTag.class);
				constructor.setAccessible(true);
				Object obj = constructor.newInstance(getPlugin());
				if (obj instanceof NPCHooks) {
					hooks = (NPCHooks) obj;
				} else {
					hooks = new NPCHooks();
				}
			} catch (Exception ex) {
				hooks = new NPCHooks();
			}
		}
		
		return hooks;
	}
	
	private final static String PLUGIN_NAME = "CombatTagReloaded"; 
	public static CombatTag getPlugin() {
		Plugin rawPlugin = Bukkit.getPluginManager().getPlugin(PLUGIN_NAME);
		if (rawPlugin != null && rawPlugin instanceof CombatTag) {
			return (CombatTag) rawPlugin;
		} else {
			return null;
		}
	}
}
