
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.gkovalechyn.raffle.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author gkovalechyn
 * Created on 06/07/2016, 21:21:32
 */
public class FlexibleCommand extends Command{
    private CommandExecutor executor;

    public FlexibleCommand(String name) {
        this(name, null);
    }
    
    public FlexibleCommand(String name, CommandExecutor executor) {
        super(name);
        this.executor = executor;
    }
    
    
    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (this.executor != null){
            return this.executor.onCommand(sender, this, label, args);
        }
        
        return true;
    }

    public CommandExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(CommandExecutor executor) {
        this.executor = executor;
    }

}
