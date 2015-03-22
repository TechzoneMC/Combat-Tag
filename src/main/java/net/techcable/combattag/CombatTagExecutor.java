package net.techcable.combattag;

import lombok.RequiredArgsConstructor;
import net.techcable.npclib.NPC;
import net.techcable.techutils.entity.UUIDUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Executes the /ct command
 *
 * Supported sub commands:
 * /ct force [player]
 * /ct wipe
 */
@RequiredArgsConstructor
public class CombatTagExecutor implements CommandExecutor {
    private final CombatTag plugin;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!label.equalsIgnoreCase("ct") && !label.equalsIgnoreCase("combattag")) return false;
        if (args.length == 0) { //Ct
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage("You must be a player to execute this command");
                return true;
            }
            onTagCheck((Player)commandSender);
            return true;
        }
        String subCommand = args[0];
        if (subCommand.equalsIgnoreCase("force")) {
            if (!commandSender.hasPermission("combattag.force")) {
                commandSender.sendMessage("You don't have permission to force someone into combat");
                return true;
            }
            CombatPlayer toForce = null;
            if (args.length == 2) {
                Player player = UUIDUtils.getPlayerExact(args[1]);
                if (player == null) {
                    commandSender.sendMessage("Please specify an online player");
                    return true;
                } else {
                    toForce = CombatPlayer.getPlayer(player);
                }
            } else if (commandSender instanceof Player) {
                toForce = CombatPlayer.getPlayer((Player) commandSender);
            } else {
                commandSender.sendMessage("Please specify an online player to tag");
                return true;
            }
            onTagForce(toForce, commandSender);
            return true;
        } else if (subCommand.equalsIgnoreCase("wipe")) {
            if (!commandSender.hasPermission("combattag.wipe")) {
                commandSender.sendMessage("You don't have permission to despawn all npcs");
                return true;
            }
            onWipe();
            return true;
        } else {
            commandSender.sendMessage("Unknown sub command");
            return true;
        }
    }

    public void onTagCheck(Player player) {
        CombatPlayer combatPlayer = CombatPlayer.getPlayer(player);
        if (combatPlayer.isTagged()) {
            player.sendMessage(plugin.getMessages().getCommandMessageTagged((int) (combatPlayer.getRemainingTagTime() / 1000)));
        } else {
            player.sendMessage(plugin.getMessages().getCommandMessageNotTagged());
        }
    }

    public static final long FORCE_TAG_TIME = 60000;
    public void onTagForce(CombatPlayer toForce, CommandSender sender) {
        if (toForce.isTagged()) {
            toForce.untag();
            if (toForce.getEntity() != sender) toForce.getEntity().sendMessage("You have been un tagged by an admin");
            sender.sendMessage("Un Tagged " + toForce.getName());
        } else {
            toForce.tag(FORCE_TAG_TIME);
            if (toForce.getEntity() != sender) toForce.getEntity().sendMessage("You have been tagged for 1 minute by an admin");
            sender.sendMessage("Tagged " + toForce.getName() + " for one minute");
        }
    }

    public void onWipe() {
        for (NPC npc : NPCManager.getInstance().getRegistry().listNpcs()) {
            NPCManager.getInstance().despawn(npc);
        }
    }
}