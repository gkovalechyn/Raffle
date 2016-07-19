
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.gkovalechyn.raffle.commands;

import net.gkovalechyn.raffle.Message;
import net.gkovalechyn.raffle.Raffle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author gkovalechyn
 * Created on Jul 19, 2016, 10:12:00 PM
 */
public class GuiCommand implements ICommand{

    @Override
    public void execute(CommandSender sender, String[] args, Raffle plugin) {
        Player player = (Player) sender;
        player.openInventory(plugin.getInventoryManager().getInventoryByPage(0).getInv());
    }

    @Override
    public boolean supportsConsole() {
        return false;
    }

    @Override
    public String getPermission() {
        return "Raffle.Cmd.GUI";
    }

    @Override
    public String getDescription() {
        return Message.CMD_GUI_DESC.getText();
    }

}
