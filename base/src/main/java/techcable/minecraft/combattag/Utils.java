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

    private static NPCHooks npcHooks;
    private final static Class<? extends NPCHooks> NPC_HOOKS_CLASS = makeClass(NPCHooks.class, "techcable.minecraft.combattag.npc.NPCHooksImpl");
	public static NPCHooks getNPCHooks() {
		if (npcHooks == null) {
			NPCHooks realImplementation = tryMakeHook(NPCHooks.class, getPlugin());
			if (realImplementation == null) {
				npcHooks = new NPCHooks();
			} else {
				npcHooks = realImplementation;
			}	
		}
		
		return npcHooks;
	}
	private final static Class<? extends InstakillHooks> INSTAKILL_HOOKS_CLASS = makeClass(InstakillHooks.class ,"techcable.minecraft.combattag.instakill.InstakillHooksImpl");
	private static InstakillHooks instakillHooks;
	public static InstakillHooks getInstakillHooks() {
		if (instakillHooks == null) {
			InstakillHooks realImplementation = tryMakeHook(InstakillHooks.class, getPlugin());
			if (realImplementation == null) {
				instakillHooks = new InstakillHooks();
			} else {
				instakillHooks = realImplementation;
			}
		}
		return instakillHooks;
	}
	
	public static <T> T tryMakeHook(Class<? extends T> hookClass, CombatTag plugin) {
		if (hookClass == null) return null;
		try {
			Constructor<? extends T> constructor = hookClass.getConstructor(CombatTag.class);
			constructor.setAccessible(true);
			return constructor.newInstance(plugin);
		} catch (Exception ex) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Class<? extends T> makeClass(Class<T> hookType, String className) {
		try {
			return (Class<? extends T>) Class.forName(className);
		} catch (Exception ex) {
			return null;
		}
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
