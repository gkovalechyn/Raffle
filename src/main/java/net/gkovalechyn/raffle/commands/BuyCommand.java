
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
import org.bukkit.entity.Player;

/**
 * @author gkovalechyn
 * Created on 07/07/2016, 16:55:00
 */
public class BuyCommand implements ICommand{

    //raffle buy <player> <amount>
    @Override
    public void execute(CommandSender sender, String[] args, Raffle plugin) {
        if (args.length < 3){
            sender.sendMessage(Message.CMD_BUY_USAGE.getText());
            return;
        }
        Player target = plugin.getServer().getPlayer(args[1]);
        int amountToBuy = 0;
        RaffleData toBuyFrom = null;
        
        try{
            amountToBuy = Integer.parseInt(args[2]);
        }catch(NumberFormatException e){
            sender.sendMessage(Message.CMD_BUY_USAGE.getText());
            return;
        }
            
        if (target == null){
            for(Map.Entry<UUID, RaffleData> entry : plugin.getRaffleManager().getRaffles().entrySet()){
                if (args[0].equals(entry.getValue().getOwnerName())){
                    toBuyFrom = entry.getValue();
                    break;
                }
            }
            
            if (toBuyFrom == null){
                sender.sendMessage(Message.ERROR_NO_RAFFLE_OTHER.getText());
                return;
            }
        }else{
            if (!plugin.getRaffleManager().hasRaffle(target.getUniqueId())){
                sender.sendMessage(Message.ERROR_NO_RAFFLE_OTHER.getText());
                return;
            }
            
            toBuyFrom = plugin.getRaffleManager().getRaffle(target.getUniqueId());
        }
        
        if (!plugin.getEconomy().has((Player) sender, amountToBuy * toBuyFrom.getPrice())){
            sender.sendMessage(Message.ERROR_NO_MONEY.getText());
            return;
        }
        
        if (toBuyFrom.areTicketsAvailable()){
            if (toBuyFrom.getTicketAmount() - toBuyFrom.getSoldTickets() >= amountToBuy){
                toBuyFrom.buyTicket(((Player) sender).getUniqueId(), amountToBuy);
                plugin.getEconomy().withdrawPlayer((Player) sender, amountToBuy * toBuyFrom.getPrice());
                sender.sendMessage(Message.CMD_BUY_BOUGHT.getText());
                //Leave it to the checker thread to run the raffle
            }else{
                sender.sendMessage(Message.CMD_BUY_CANTBUYAMOUNT.getText());
            }
        }else{
            sender.sendMessage(Message.CMD_BUY_CANTBUY.getText());
        }
    }

    @Override
    public boolean supportsConsole() {
        return false;
    }

    @Override
    public String getPermission() {
        return "Raffle.Cmds.Buy";
    }

    @Override
    public String getDescription() {
        return Message.CMD_BUY_DESC.getText();
    }

}
