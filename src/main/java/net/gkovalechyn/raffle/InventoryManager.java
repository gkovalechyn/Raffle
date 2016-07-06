/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.raffle;

import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

/**
 *
 * @author gkovalechyn
 */
public class InventoryManager {
    private final Raffle plugin;
    private InventoryWrapper[] inventories;

    public InventoryManager(Raffle plugin) {
        this.plugin = plugin;
    }
    
    public boolean isOurInventory(Inventory inv){
        if (this.inventories != null && this.inventories.length > 0){
            for(InventoryWrapper data : this.inventories){
                if (data.inventory.equals(inv)){
                    return true;
                }
            }
            
            return false;
        }else{
            return false;
        }
    }
    
    public Inventory getInventoryForPage(int page){
        if (this.inventories == null || this.inventories.length == 0){
            this.rebuildInventories(this.plugin.getRaffleManager().getRaffles());
        }
        
        if (page > this.inventories.length || page < 0){
            page = 0;
        }
        
        return this.inventories[page].inventory;
    }
    
    public void rebuildInventories(Map<UUID, RaffleData> raffles){
        int totalPageCount = (int) Math.ceil(raffles.size() / 3d);
        
        if (totalPageCount == 0){
            totalPageCount = 1;
        }
        
        this.inventories = new InventoryWrapper[totalPageCount];
        
        for(int i = 0; i < totalPageCount; i++){
            Inventory inv = Bukkit.createInventory(null, 54, Message.INV_NAME.getTextReplaced("{Page}", "" + i));
            InventoryWrapper data = new InventoryWrapper(inv, i);
            
            //45 and 53. Go back<-> forth between the pages
            
            this.inventories[i] = data;
        }
    }
}
