/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.raffle;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import net.gkovalechyn.raffle.commands.FlexibleCommand;
import net.gkovalechyn.raffle.util.IdDataWrapper;
import org.bukkit.command.CommandMap;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author gkovalechyn
 */
public class Raffle extends JavaPlugin{
    private RaffleManager raffleManager;
    private InventoryManager inventoryManager;
    
    private final List<IdDataWrapper> blockedItems = new ArrayList<>();
    
    @Override
    public void onEnable(){
        
    }
    
    @Override
    public void onDisable(){
        
    }

    public RaffleManager getRaffleManager() {
        return raffleManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }
    
    public boolean isItemBlocked(int itemID, byte data){
        for(IdDataWrapper wrapper : this.blockedItems){
            if (itemID == wrapper.id){
                if (wrapper.data == 0){
                    return true;
                }else if (wrapper.data == data){
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private void registerCustomCommand(String command, FlexibleCommand fc){
        try {
            Field commandMapField = CraftServer.class.getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap cm = (CommandMap) commandMapField.get(this.getServer());
            cm.register(command, fc);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
