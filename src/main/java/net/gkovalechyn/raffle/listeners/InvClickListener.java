
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.gkovalechyn.raffle.listeners;

import net.gkovalechyn.raffle.Raffle;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author gkovalechyn
 * Created on 09/07/2016, 15:58:51
 */
public class InvClickListener implements Listener{
    private final Raffle plugin;

    public InvClickListener(Raffle plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClicked(InventoryClickEvent evt){
        if (this.plugin.getInventoryManager().isOurInventory(evt.getInventory())){
            evt.setCancelled(true);
            evt.setCurrentItem(new ItemStack(Material.AIR));
        }
        
    }
}
