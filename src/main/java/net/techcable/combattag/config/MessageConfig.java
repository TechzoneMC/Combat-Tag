package net.techcable.combattag.config;

import com.trc202.settings.Settings;
import lombok.Getter;
import net.techcable.combattag.CombatTag;
import net.techcable.techutils.yamler.Comment;
import net.techcable.techutils.yamler.Comments;
import net.techcable.techutils.yamler.Config;
import org.bukkit.ChatColor;

import java.io.File;
import java.text.MessageFormat;

public class MessageConfig extends Config {
    @Getter
    private final CombatTag plugin;
    public MessageConfig(CombatTag plugin) {
        this.plugin = plugin;
        File configDir = new File(plugin.getDataFolder().getParent(), "CombatTag");
        configDir.mkdirs();
        CONFIG_FILE = new File(configDir, "messages.yml");
        CONFIG_HEADER = new String[] {
                "CombatTagReloaded Messages",
                "Color Codes are supported with the prefix '&'"
        };
    }

    private String cantEnterSafezoneInCombat = "[CombatTag] you can't enter a safezone in combat";
    @Comments({
            "The message sent to the attacker when he is combat tagged",
            "{0} is replaced with the name of the defender"
    })
    private String tagMessageDamager = "[CombatTag] You have hit {0}. Type /ct to check your remaining tag time.";
    @Comments({
            "The message sent to the defender when he is combat tagged",
            "{0} is replaced with the name of the attacker"
    })
    private String tagMessageDamaged = "[CombatTag] You have been hit by {0}. Type /ct to check your remaining tag time.";
    private String cantTagInCreative = "[CombatTag] You can't combat tag in creative mode";
    private String cantPlaceBlocksInCombat = "[CombatTag] You can't place blocks in combat";
    private String cantBreakBlocksInCombat = "[CombatTag] You can't break blocks in combat";
    private String cantTeleportInCombat= "[CombatTag] You can't teleport in combat";
    private String cantEnderpearlInCombat = "[CombatTag] You can't enderpearl in combat";
    private String cantFlyInCombat = "[CombatTag] You can't fly in combat";
    private String allCommandsDisabled = "[CombatTag] All commands are disabled in combat";
    private String thisCommandDisabled = "[CombatTag] This command is disabled in combat";
    @Comments({
            "The message sent to players when /ct is executed and they are tagged",
            "{0} is replaced with the time remaining in combat"
    })
    private String commandMessageTagged = "[CombatTag] You are in combat for {0} seconds.";
    @Comment("The message sent to players when /ct is executed and they are not tagged.")
    private String commandMessageNotTagged = "[CombatTag] You are not currently in combat";

    public File getFile() {
        return CONFIG_FILE;
    }

    public String getCantEnterSafezoneInCombat() {
        return color(cantEnterSafezoneInCombat);
    }

    public String getTagMessageDamager(String name) {
        return MessageFormat.format(color(tagMessageDamager), name);
    }

    public String getTagMessageDamaged(String name) {
        return MessageFormat.format(color(tagMessageDamaged), name);
    }

    public String getCantTagInCreative() {
        return color(cantTagInCreative);
    }

    public String getCantPlaceBlocksInCombat() {
        return color(cantPlaceBlocksInCombat);
    }

    public String getCantBreakBlocksInCombat() {
        return color(cantBreakBlocksInCombat);
    }

    public String getCantTeleportInCombat() {
        return color(cantTeleportInCombat);
    }

    public String getCantEnderpearlInCombat() {
        return color(cantEnderpearlInCombat);
    }

    public String getCantFlyInCombat() {
        return color(cantFlyInCombat);
    }

    public String getAllCommandsDisabled() {
        return color(allCommandsDisabled);
    }

    public String getThisCommandDisabled() {
        return color(thisCommandDisabled);
    }

    public String getCommandMessageTagged(int remainingTime) { //Arg is string so MainConfig can implement ISettings
        return MessageFormat.format(color(commandMessageTagged), remainingTime);
    }

    private static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public void migrateFrom(Settings old) {
        this.commandMessageTagged = "[CombatTag] " + old.getCommandMessageTagged();
        this.commandMessageNotTagged = "[CombatTag] " + old.getCommandMessageNotTagged();
        this.tagMessageDamager = "[CombatTag] " + old.getTagMessageDamager();
        this.tagMessageDamaged = "[CombatTag] " + old.getTagMessageDamaged();
    }
}
