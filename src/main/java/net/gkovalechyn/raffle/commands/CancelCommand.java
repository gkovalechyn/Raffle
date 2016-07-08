
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.raffle.commands;

import java.util.Map;
import java.util.UUID;
import net.gkovalechyn.raffle.Message;
import net.gkovalechyn.raffle.Raffle;
import net.gkovalechyn.raffle.RaffleData;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * @author gkovalechyn Created on 07/07/2016, 16:05:27
 */
public class CancelCommand implements ICommand {

    @Override
    public void execute(CommandSender sender, String[] args, Raffle plugin) {
        if (args.length == 1) {
            if (sender instanceof ConsoleCommandSender) {
                sender.sendMessage(Message.ERROR_CONSOLE_NOT_SUPPORTED.getText());
                return;
            }
            Player player = (Player) sender;

            if (!plugin.getRaffleManager().hasRaffle(player.getUniqueId())) {
                sender.sendMessage(Message.ERROR_NO_RAFFLE_SELF.getText());
                return;
            }
            
            plugin.getRaffleManager().cancelRaffle(player.getUniqueId());
            sender.sendMessage(Message.CMD_CANCEL_CANCELLED.getText());
        } else if (sender.hasPermission("Raffle.Cmds.Cancel.Others")) {
            Player player = plugin.getServer().getPlayer(args[1]);
            
            if (player == null){
                for(Map.Entry<UUID, RaffleData> entry : plugin.getRaffleManager().getRaffles().entrySet()){
                    if (args[1].equals(entry.getValue().getOwnerName())){
                        plugin.getRaffleManager().cancelRaffle(entry.getKey());
                        sender.sendMessage(Message.CMD_CANCEL_CANCELLED.getText());
                        return;
                    }
                }
                sender.sendMessage(Message.ERROR_NO_RAFFLE_OTHER.getText());
            }else{
                if (!plugin.getRaffleManager().hasRaffle(player.getUniqueId())) {
                    sender.sendMessage(Message.ERROR_NO_RAFFLE_OTHER.getText());
                    return;
                }
                
                plugin.getRaffleManager().cancelRaffle(player.getUniqueId());
                sender.sendMessage(Message.CMD_CANCEL_CANCELLED.getText());
            }
        } else {
            sender.sendMessage(Message.ERROR_NO_PERMISSION.getTextReplaced("{Permission}", "Raffle.Cmds.Cancel.Others"));
        }
    }

    @Override
    public boolean supportsConsole() {
        return true;
    }

    @Override
    public String getPermission() {
        return "Raffle.Cmds.Cancel";
    }

    @Override
    public String getDescription() {
        return Message.CMD_CANCEL_DESC.getText();
    }

}
