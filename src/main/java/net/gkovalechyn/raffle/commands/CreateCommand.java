
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.gkovalechyn.raffle.commands;

import net.gkovalechyn.raffle.Message;
import net.gkovalechyn.raffle.Raffle;
import net.gkovalechyn.raffle.RaffleData;
import net.gkovalechyn.raffle.util.Util;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author gkovalechyn
 * Created on 06/07/2016, 21:11:17
 */
public class CreateCommand implements ICommand{

    // create <tickets> <preco> <duracao>
    @Override
    public void execute(CommandSender sender, String[] args, Raffle plugin) {
        if (args.length < 3){
            sender.sendMessage(Message.CMD_CREATE_USAGE.getText());
            return;
        }
        Player player = (Player) sender;
        int ticketAmount = 0;
        double cost = 0;
        long duration = 0;
        RaffleData data = null;
        ItemStack item = null;
        
        if (plugin.getRaffleManager().hasRaffle(player.getUniqueId())){
            sender.sendMessage(Message.CMD_CREATE_ALREADY_EXISTS.getText());
            return;
        }
        
        //Use getItemInHand to provide backwards-compatibility
        if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR){
            sender.sendMessage(Message.CMD_CREATE_NO_ITEM.getText());
            return;
        }else{
           item = player.getItemInHand();
            if (plugin.isItemBlocked(item.getTypeId(), item.getData().getData()) 
                    && !player.hasPermission("Raffle.Cmd.Create.BypassIDCheck")){
                sender.sendMessage(Message.CMD_CREATE_ITEM_BLOCKED.getText());
                return;
            }
        }
        
        
        try{
            ticketAmount = Integer.parseInt(args[0]);
            cost = Double.parseDouble(args[1]);
            duration = Util.timeStringToLong(args[2]);
        }catch(NumberFormatException e){
            sender.sendMessage(Message.CMD_CREATE_USAGE.getText());
            return;
        }
        
        data = new RaffleData(player.getUniqueId(), item, ticketAmount, cost, duration);
        plugin.getRaffleManager().putRaffle(player.getUniqueId(), data);
        sender.sendMessage(Message.CMD_CREATE_CREATED.getText());
    }

    @Override
    public boolean supportsConsole() {
        return false;
    }

    @Override
    public String getPermission() {
        return "Raffle.Cmd.Create";
    }

    @Override
    public String getDescription() {
        return Message.CMD_CREATE_DESC.getText();
    }

}
