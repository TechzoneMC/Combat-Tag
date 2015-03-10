package net.techcable.combattag;

import static net.techcable.combattag.Utils.severe;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.mcstats.Metrics;
import org.mcstats.Metrics.Graph;
import org.mcstats.Metrics.Plotter;

import com.trc202.settings.Settings;
import com.trc202.settings.SettingsHelper;
import com.trc202.settings.SettingsLoader;

import net.gravitydevelopment.updater.Updater;
import net.gravitydevelopment.updater.Updater.UpdateType;
import net.techcable.combattag.listeners.CompatibilityListener;
import net.techcable.combattag.listeners.InstakillListener;
import net.techcable.combattag.listeners.NPCListener;
import net.techcable.combattag.listeners.PermissionListener;
import net.techcable.combattag.listeners.PlayerListener;
import net.techcable.combattag.listeners.SettingListener;
import net.techcable.npclib.NPCLib;
import net.techcable.techutils.TechPlugin;
import lombok.*;

@Getter
public class CombatTag extends TechPlugin<CombatPlayer> {
	private Settings settings;
	private final SettingsHelper settingsHelper = new SettingsHelper(new File(getDataFolder().getParent() + File.pathSeparator + "CombatTag" + File.pathSeparator + "settings.prop"), getName());
	private NPCManager npcManager;
	
	@Override
	protected void shutdown() {
		Utils.info("Disabled");
	}

	@Override
	protected void startup() {
		this.settings = new SettingsLoader().loadSettings(getSettingsHelper(), getDescription().getVersion());
		initMetrics();
		tryUpdatePlugin();
		registerListeners();
        if (!getSettings().isInstaKill()) {
            if (!NPCLib.isSupported() ){
                severe("NPCs are enabled but this version of minecraft isn't supported");
                severe("[CombatTag] Please install citizens or update CombatTag if you want to use npcs");
                setEnabled(false);
                return;
            } else {
            	this.npcManager = new NPCManager(this);
            }
        }
        
        if (!initMetrics()) {
            getLogger().warning("Unable to initialize metrics");
        } else {
            Utils.debug("Enabled Metrics");
        }
	}
	
	@Override
	public CombatPlayer createPlayer(UUID id) {
		return new CombatPlayer(id, this);
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

    private static final int projectId = 86389;

    public void tryUpdatePlugin() {
        if (getSettings().isUpdateEnabled()) {
            @SuppressWarnings("unused")
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
