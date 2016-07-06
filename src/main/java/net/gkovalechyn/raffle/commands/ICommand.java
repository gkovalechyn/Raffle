
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.gkovalechyn.raffle.commands;

import net.gkovalechyn.raffle.Raffle;
import org.bukkit.command.CommandSender;

/**
 * @author gkovalechyn
 * Created on 06/07/2016, 20:52:25
 */
public interface ICommand {
    
    public void execute(CommandSender sender, String[] args, Raffle plugin);
    
    public boolean supportsConsole();
    
    public String getPermission();
    
    public String getDescription();
}