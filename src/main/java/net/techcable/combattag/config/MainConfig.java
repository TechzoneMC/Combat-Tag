package net.techcable.combattag.config;

import com.trc202.settings.Settings;

import net.techcable.combattag.CombatTag;
import net.techcable.techutils.yamler.Comment;
import net.techcable.techutils.yamler.Comments;
import net.techcable.techutils.yamler.Config;

import java.io.File;
import java.util.ArrayList;

/**
 * Represents the main Combat-Tag Configuration
 *
 * @author Techcable
 */
public class MainConfig extends Config {
    private final CombatTag plugin;

    public MainConfig(CombatTag plugin) {
        this.plugin = plugin;
        File configDir = new File(plugin.getDataFolder().getParent(), "CombatTag");
        configDir.mkdirs();
        CONFIG_FILE = new File(configDir, "config.yml");
        CONFIG_HEADER = new String[] {"CombatTagReloaded Configuration"};
    }

    @Comment("Set to true to dissalow players to enter safezones in combat")
    private boolean stopCombatSafezoning = false;
    @Comment("The amount of time combat tagging lasts")
    private int tag_duration = 10;
    @Comment("Enable debuging")
    private boolean debugEnabled = false;
    @Comments({
            "Set to true to kill players when they log out in combat",
            "Set to false to spawn an npc when players log out in combat"
    })
    private boolean tag_instaKill = false;
    @Comment("Commands not allowed in combat")
    private ArrayList<String> tag_disabledCommands = new ArrayList<>();
    @Comment("Worlds in which combat tag reloaded is disabled")
    private ArrayList<String> disallowedWorlds = new ArrayList<>();
    @Comments({
            "The name of the npc that is spawned when players log out",
            "player is replaced with the name of the combat logger",
            "number is replaced with the npc's number",
            "If neither player nor number is in the name, number will be added to the end"
    })
    private String npc_name = "name";
    @Comment("Set to false to dissalow players to break and place blocks in combat")
    private boolean tag_allowBlockEdit = true;
    @Comments({
            "The amount of time until npcs despawn",
            "A value less than 0 means npcs will only despawn if the server restarts or the pvplogger logs back in"
    })
    private int npc_despawnTime = -1;
    @Comment("Set to true to kill the npc after npc.despawnTime runs out")
    private boolean npc_dieAfterTime = false;
    @Comment("Set to false to punish players even if they are kicked")
    private boolean tag_dropTagOnKick = true;
    @Comments({
            "Disable teleport in combat",
            "WARNING: This may interfere with other plugins, disabling a command is the prefered solution",
    })
    private boolean tag_blockTeleport = false;
    @Comment("Disable ender peral in combat")
    private boolean tag_blockEnderPearl = false;
    // private boolean dontSpawnInWG; -- Not Used
    @Comment("Set to true to only combat tag the attacker")
    private boolean tag_onlyDamagerTagged = false;
    @Comment("Set to true to allow mobs to combat tag players")
    private boolean tag_mobTag = false;
    // private boolean playerTag; -- Always true
    @Comment("Set to false to allow creative players to tag survival players")
    private boolean tag_blockCreativeTagging = true;
    @Comment("Set to true to allow flying in combat")
    private boolean tag_blockFly = false;
    @Comment("Set to true to enable updating")
    private boolean updateEnabled = true;
    @Comment("Set to true to disable disguises in combat")
    private boolean tag_disableDisguises = false;

    public void migrateFrom(Settings oldSettings) {
        setDisableDisguisesInCombat(oldSettings.isDisableDisguisesInCombat());
        setStopCombatSafezoning(oldSettings.isStopCombatSafezoning());
        setUpdateEnabled(oldSettings.isUpdateEnabled());
        setInstaKill(oldSettings.isInstaKill());
        setTagDuration(oldSettings.getTagDuration());
        setDebugEnabled(oldSettings.isDebugEnabled());
        setDisabledCommands(oldSettings.getDisabledCommands());
        setDisallowedWorlds(oldSettings.getDisallowedWorlds());
        setNpcName(oldSettings.getNpcName());
        setAllowBlockEditInCombat(oldSettings.isBlockEditWhileTagged());
        setNpcDespawnTime(oldSettings.getNpcDespawnTime());
        setNpcDieAfterTime(oldSettings.isNpcDieAfterTime());
        setDropTagOnKick(oldSettings.isDropTagOnKick());
        setBlockTeleportInCombat(oldSettings.isBlockTeleport());
        setBlockEnderPearlInCombat(oldSettings.isBlockEnderPearl());
        setOnlyDamagerTagged(oldSettings.isOnlyDamagerTagged());
        setTagMobs(oldSettings.isMobTag());
        setBlockCreativeTagging(oldSettings.isBlockCreativeTagging());
        setBlockFlyInCombat(oldSettings.isBlockFly());
        setUpdateEnabled(oldSettings.isUpdateEnabled());
        setDisableDisguisesInCombat(oldSettings.isDisableDisguisesInCombat());
    }

    public File getFile() {
        return CONFIG_FILE;
    }

    //Getters and setters without the weird names

    public boolean isStopCombatSafezoning() {
        return stopCombatSafezoning;
    }

    public void setStopCombatSafezoning(boolean stopCombatSafezoning) {
        this.stopCombatSafezoning = stopCombatSafezoning;
    }

    public int getTagDuration() {
        return tag_duration;
    }

    public void setTagDuration(int tag_duration) {
        this.tag_duration = tag_duration;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }

    public boolean isInstaKill() {
        return tag_instaKill;
    }

    public void setInstaKill(boolean tag_instaKill) {
        this.tag_instaKill = tag_instaKill;
    }

    public ArrayList<String> getDisabledCommands() {
        return tag_disabledCommands;
    }

    public void setDisabledCommands(ArrayList<String> tag_disabledCommands) {
        this.tag_disabledCommands = tag_disabledCommands;
    }

    public ArrayList<String> getDisallowedWorlds() {
        return disallowedWorlds;
    }

    public void setDisallowedWorlds(ArrayList<String> disallowedWorlds) {
        this.disallowedWorlds = disallowedWorlds;
    }

    public String getNpcName() {
        return npc_name;
    }

    public void setNpcName(String npc_name) {
        this.npc_name = npc_name;
    }

    public boolean isAllowBlockEditInCombat() {
        return tag_allowBlockEdit;
    }

    public void setAllowBlockEditInCombat(boolean tag_allowBlockEdit) {
        this.tag_allowBlockEdit = tag_allowBlockEdit;
    }

    public int getNpcDespawnTime() {
        return npc_despawnTime;
    }

    public void setNpcDespawnTime(int npc_despawnTime) {
        this.npc_despawnTime = npc_despawnTime;
    }

    public boolean isNpcDieAfterTime() {
        return npc_dieAfterTime;
    }

    public void setNpcDieAfterTime(boolean npc_dieAfterTime) {
        this.npc_dieAfterTime = npc_dieAfterTime;
    }

    public boolean isDropTagOnKick() {
        return tag_dropTagOnKick;
    }

    public void setDropTagOnKick(boolean tag_dropTagOnKick) {
        this.tag_dropTagOnKick = tag_dropTagOnKick;
    }

    public boolean isBlockTeleportInCombat() {
        return tag_blockTeleport;
    }

    public void setBlockTeleportInCombat(boolean tag_blockTeleport) {
        this.tag_blockTeleport = tag_blockTeleport;
    }

    public boolean isBlockEnderPearlInCombat() {
        return tag_blockEnderPearl;
    }

    public void setBlockEnderPearlInCombat(boolean tag_blockEnderPearl) {
        this.tag_blockEnderPearl = tag_blockEnderPearl;
    }

    public boolean isOnlyDamagerTagged() {
        return tag_onlyDamagerTagged;
    }

    public void setOnlyDamagerTagged(boolean tag_onlyDamagerTagged) {
        this.tag_onlyDamagerTagged = tag_onlyDamagerTagged;
    }

    public boolean isTagMobs() {
        return tag_mobTag;
    }

    public void setTagMobs(boolean tag_mobTag) {
        this.tag_mobTag = tag_mobTag;
    }

    public boolean isBlockCreativeTagging() {
        return tag_blockCreativeTagging;
    }

    public void setBlockCreativeTagging(boolean tag_blockCreativeTagging) {
        this.tag_blockCreativeTagging = tag_blockCreativeTagging;
    }

    public boolean isBlockFlyInCombat() {
        return tag_blockFly;
    }

    public void setBlockFlyInCombat(boolean tag_blockFly) {
        this.tag_blockFly = tag_blockFly;
    }

    public boolean isUpdateEnabled() {
        return updateEnabled;
    }

    public void setUpdateEnabled(boolean updateEnabled) {
        this.updateEnabled = updateEnabled;
    }

    public boolean isDisableDisguisesInCombat() {
        return tag_disableDisguises;
    }

    public void setDisableDisguisesInCombat(boolean tag_disableDisguises) {
        this.tag_disableDisguises = tag_disableDisguises;
    }
}