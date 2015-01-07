package com.trc202.CombatTag;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import net.gravitydevelopment.updater.Updater;
import net.gravitydevelopment.updater.Updater.UpdateType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.mcstats.Metrics;
import org.mcstats.Metrics.Graph;
import org.mcstats.Metrics.Plotter;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.trc202.settings.Settings;
import com.trc202.settings.SettingsHelper;
import com.trc202.settings.SettingsLoader;

import techcable.minecraft.combattag.Utils;

import static techcable.minecraft.combattag.Utils.info;
import static techcable.minecraft.combattag.Utils.warning;
import static techcable.minecraft.combattag.Utils.severe;

import techcable.minecraft.combattag.entity.CombatTagNPC;
import techcable.minecraft.combattag.entity.CombatTagPlayer;
import techcable.minecraft.combattag.listeners.CompatibilityListener;
import techcable.minecraft.combattag.listeners.InstakillListener;
import techcable.minecraft.combattag.listeners.NPCListener;
import techcable.minecraft.combattag.listeners.PermissionListener;
import techcable.minecraft.combattag.listeners.PlayerListener;
import techcable.minecraft.combattag.listeners.SettingListener;
import techcable.minecraft.npclib.NPC;
import techcable.minecraft.npclib.NPCLib;
import techcable.minecraft.techutils.TechUtils;

import lombok.Getter;

public class CombatTag extends JavaPlugin {

    private final SettingsHelper settingsHelper;
    private final File settingsFile;
    public Settings settings;
    private HashMap<UUID, Long> tagged;
    private static final String mainDirectory = "plugins/CombatTag";
    private static final List<String> SUBCOMMANDS = ImmutableList.of("reload", "wipe", "command");
    private static final List<String> COMMAND_SUBCOMMANDS = ImmutableList.of("add", "remove");

    //public final CombatTagIncompatibles ctIncompatible = new CombatTagIncompatibles(this);

    private int npcNumber;
    public CombatTag() {
        settings = new Settings();
        new File(mainDirectory).mkdirs();
        settingsFile = new File(mainDirectory + File.separator + "settings.prop");
        settingsHelper = new SettingsHelper(settingsFile, "CombatTag");
        npcNumber = 0;
    }

    /**
     * Change SL in NPCManager to:
     *
     * private class SL implements Listener {
     *
     * @SuppressWarnings("unused") public void disableNPCLib() { despawnAll();
     * Bukkit.getServer().getScheduler().cancelTask(taskid); } }
     */
    /**
     * Change NullSocket to:
     *
     * class NullSocket extends Socket { private final byte[] buffer = new
     * byte[50];
     *
     * @Override public InputStream getInputStream() { return new
     * ByteArrayInputStream(this.buffer); }
     *
     * @Override public OutputStream getOutputStream() { return new
     * ByteArrayOutputStream(10); } }
     */
    @Override
    public void onDisable() {
        for (CombatTagNPC npc : CombatTagNPC.getAllNpcs()) {
        	npc.despawn();
        }
        disableMetrics();
        //Just in case...
        info("Disabled");
    }

    @Override
    public void onEnable() {
        settings = new SettingsLoader().loadSettings(settingsHelper, this.getDescription().getVersion());
        if (!settings.isInstaKill()) {
            if (!NPCLib.isSupported() ){
                severe("NPCs are enabled but this version of minecraft isn't supported");
                severe("[CombatTag] Please install citizens or update CombatTag if you want to use npcs");
                setEnabled(false);
                return;
            }
        }
        TechUtils.setDebug(settings.isDebugEnabled());
        tagged = new HashMap<UUID, Long>();
        PluginManager pm = getServer().getPluginManager();
        //ctIncompatible.startup(pm);
        if (!initMetrics()) {
            getLogger().warning("Unable to initialize metrics");
        } else {
            if (isDebugEnabled()) getLogger().info("Enabled Metrics");
        }
        registerListeners();

        info("has loaded with a tag time of " + settings.getTagDuration() + " seconds");
    }

    public long getRemainingTagTime(UUID uuid) {
        if (tagged.get(uuid) == null) {
            return -1;
        }
        return (tagged.get(uuid) - System.currentTimeMillis());
    }

    public boolean addTagged(Player player) {
        if (player.isOnline()) {
            tagged.remove(player.getUniqueId());
            tagged.put(player.getUniqueId(), PvPTimeout(getTagDuration()));
            return true;
        }
        return false;
    }

    public boolean inTagged(UUID name) {
        return tagged.containsKey(name);
    }

    public long removeTagged(UUID name) {
        if (inTagged(name)) {
            return tagged.remove(name);
        }
        return -1;
    }

    public long PvPTimeout(int seconds) {
        return System.currentTimeMillis() + (seconds * 1000);
    }

    public boolean isInCombat(UUID uuid) {
        if (getRemainingTagTime(uuid) < 0) {
            tagged.remove(uuid);
            return false;
        } else {
            return true;
        }
    }

    public String getNpcName(String plr) {
        String npcName = settings.getNpcName();
        if (!(npcName.contains("player") || npcName.contains("number"))) {
            npcName = npcName + getNpcNumber();
        }
        if (npcName.contains("player")) {
            npcName = npcName.replace("player", plr);
        }
        if (npcName.contains("number")) {
            npcName = npcName.replace("number", "" + getNpcNumber());
        }
        return npcName;
    }
    /**
     *
     * @return the system tag duration as set by the user
     */
    public int getTagDuration() {
        return settings.getTagDuration();
    }

    public boolean isDebugEnabled() {
        return settings.isDebugEnabled();
    }

    public void emptyInventory(Player target) {
        PlayerInventory targetInv = target.getInventory();
        targetInv.clear();
        if (isDebugEnabled()) {
            info("[CombatTag] " + target.getName() + " has been killed by Combat Tag and their inventory has been emptied through UpdatePlayerData.");
        }
    }

    public int getNpcNumber() {
        npcNumber = npcNumber + 1;
        return npcNumber;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (command.getName().equalsIgnoreCase("ct") || (command.getName().equalsIgnoreCase("combattag"))) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    if (isInCombat(((Player) sender).getUniqueId())) {
                        String message = settings.getCommandMessageTagged();
                        message = message.replace("[time]", "" + (getRemainingTagTime(((Player) sender).getUniqueId()) / 1000));
                        sender.sendMessage(message);
                    } else {
                        tagged.remove(((Player) sender).getUniqueId());
                        sender.sendMessage(settings.getCommandMessageNotTagged());
                    }
                } else {
                    info("[CombatTag] /ct can only be used by a player!");
                }
                return true;
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("combattag.reload")) {
                    settings = new SettingsLoader().loadSettings(settingsHelper, this.getDescription().getVersion());
                    if (sender instanceof Player) {
                        sender.sendMessage(ChatColor.RED + "[CombatTag] Settings were reloaded!");
                    } else {
                        info("[CombatTag] Settings were reloaded!");
                    }
                } else {
                    if (sender instanceof Player) {
                        sender.sendMessage(ChatColor.RED + "[CombatTag] You don't have the permission 'combattag.reload'!");
                    }
                }
                return true;
            } else if (args[0].equalsIgnoreCase("wipe")) {
                if (!NPCLib.isSupported()) {
                	sender.sendMessage(ChatColor.RED + "[CombatTag] npcs aren't supported");
                } else if (sender.hasPermission("combattag.wipe")) {
                    int numNPC = 0;
                    for (CombatTagNPC npc : CombatTagNPC.getAllNpcs()) {
                    	npc.despawn();
                    }
                    sender.sendMessage("[CombatTag] Wiped " + numNPC + " pvploggers!");
                }
                return true;
            } else if (args[0].equalsIgnoreCase("command")) {
                if (sender.hasPermission("combattag.command")) {
                    if (args.length > 2) {
                        if (args[1].equalsIgnoreCase("add")) {
                            if (args[2].length() == 0 || !args[2].startsWith("/")) {
                                sender.sendMessage(ChatColor.RED + "[CombatTag] Correct Usage: /ct command add /<command>");
                            } else {
                                String disabledCommands = settingsHelper.getProperty("disabledCommands");
                                if (!disabledCommands.contains(args[2])) {
                                    disabledCommands = disabledCommands.substring(0, disabledCommands.length() - 1) + "," + args[2] + "]";
                                    disabledCommands = disabledCommands.replace("[,", "[");
                                    disabledCommands = disabledCommands.replaceAll(",,", ",");
                                    settingsHelper.setProperty("disabledCommands", disabledCommands);
                                    settingsHelper.saveConfig();
                                    sender.sendMessage(ChatColor.RED + "[CombatTag] Added " + args[2] + " to combat blocked commands.");
                                    settings = new SettingsLoader().loadSettings(settingsHelper, this.getDescription().getVersion());
                                } else {
                                    sender.sendMessage(ChatColor.RED + "[CombatTag] That command is already in the blocked commands list.");
                                }
                            }
                        } else if (args[1].equalsIgnoreCase("remove")) {
                            if (args[2].length() == 0 || !args[2].startsWith("/")) {
                                sender.sendMessage(ChatColor.RED + "[CombatTag] Correct Usage: /ct command remove /<command>");
                            } else {
                                String disabledCommands = settingsHelper.getProperty("disabledCommands");
                                if (disabledCommands.contains(args[2] + ",") || disabledCommands.contains(args[2] + "]")) {
                                    disabledCommands = disabledCommands.replace(args[2] + ",", "");
                                    disabledCommands = disabledCommands.replace(args[2] + "]", "]");
                                    disabledCommands = disabledCommands.replace(",]", "]");
                                    disabledCommands = disabledCommands.replaceAll(",,", ",");
                                    settingsHelper.setProperty("disabledCommands", disabledCommands);
                                    settingsHelper.saveConfig();
                                    sender.sendMessage(ChatColor.RED + "[CombatTag] Removed " + args[2] + " from combat blocked commands.");
                                    settings = new SettingsLoader().loadSettings(settingsHelper, this.getDescription().getVersion());
                                } else {
                                    sender.sendMessage(ChatColor.RED + "[CombatTag] That command is not in the blocked commands list.");
                                }
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "[CombatTag] Correct Usage: /ct command <add/remove> /<command>");
                    }
                }
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "[CombatTag] That is not a valid command!");
                return true;
            }
        }
        return false;
    }

    @Override
    public final List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], SUBCOMMANDS, new ArrayList<String>(SUBCOMMANDS.size()));
        } else if (args.length == 2) {
            System.out.println(args[1]);
            if (args[0].equalsIgnoreCase("command")) {
                return StringUtil.copyPartialMatches(args[1], COMMAND_SUBCOMMANDS, new ArrayList<String>(COMMAND_SUBCOMMANDS.size()));
            }
        }
        return ImmutableList.of();
    }

    public double healthCheck(double health) {
        if (health < 0) {
            health = 0;
        }
        if (health > 20) {
            health = 20;
        }
        return health;
    }

    public SettingsHelper getSettingsHelper() {
        return this.settingsHelper;
    }

    private Metrics metrics;
    public boolean initMetrics() {
        try {
            if (metrics == null) {
                metrics = new Metrics(this);
            }
            Graph punishment = metrics.createGraph("Punishment used on Combat Tag");

            punishment.addPlotter(new Plotter("Instakill") {

                @Override
                public int getValue() {
                    if (settings.isInstaKill()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });

            punishment.addPlotter(new Plotter("NPC") {
                @Override
                public int getValue() {
                    if (!settings.isInstaKill()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
            metrics.start();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public void disableMetrics() {

    }

    private static final int projectId = 86389;

    public void updateProject() {
        if (settings.isUpdateEnabled()) {
            Updater updater = new Updater(this, CombatTag.projectId, this.getFile(), UpdateType.DEFAULT, true);
        }
    }
    public void registerListeners() {
    	PluginManager manager = Bukkit.getPluginManager();
    	if (settings.isInstaKill()) {
    		manager.registerEvents(new InstakillListener(), this);
    	} else {
    		manager.registerEvents(new NPCListener(), this);
    	}
    	manager.registerEvents(new PermissionListener(), this);
    	manager.registerEvents(new CompatibilityListener(), this);
    	manager.registerEvents(new PlayerListener(), this);
    	manager.registerEvents(new SettingListener(), this);
    }
}
