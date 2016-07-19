
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
import net.gkovalechyn.raffle.util.Util;
import org.bukkit.command.CommandSender;

/**
 * @author gkovalechyn
 * Created on 07/07/2016, 17:14:42
 */
public class ListCommand implements ICommand{
    
    @Override
    public void execute(CommandSender sender, String[] args, Raffle plugin) {
        sender.sendMessage(Message.GENERAL_HEADER.getText());
        
        for(Map.Entry<UUID, RaffleData> entry : plugin.getRaffleManager().getRaffles().entrySet()){
            RaffleData data = entry.getValue();
            sender.sendMessage(Message.CMD_LIST_FORMAT.getTextReplaced(
                    "{Name}", data.getOwnerName(),
                    "{Sold}", Integer.toString(data.getSoldTickets()),
                    "{Total}", Integer.toString(data.getTicketAmount()),
                    "{Cost}", String.format("%.3f", data.getPrice()),
                    "{Remaining}", Util.timespanToString(data.getStart() + data.getDuration() - System.currentTimeMillis())
                    ));
        }
        
        sender.sendMessage(Message.GENERAL_FOOTER.getText());
    }

    @Override
    public boolean supportsConsole() {
        return true;
    }

    @Override
    public String getPermission() {
        return "Raffle.Cmds.List";
    }

    @Override
    public String getDescription() {
        return Message.CMD_LIST_DESC.getText();
    }

}
