
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.gkovalechyn.raffle.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.gkovalechyn.raffle.Raffle;
import net.gkovalechyn.raffle.RaffleData;
import org.bukkit.inventory.Inventory;

/**
 * @author gkovalechyn
 * Created on 09/07/2016, 15:30:12
 */
public class InventoryManager {
    private InventoryWrapper[] inventories = null;
    private Map<RaffleData, InventoryWrapper> dataInvMap = new HashMap<>();
    private final Raffle plugin;

    public InventoryManager(Raffle plugin) {
        this.plugin = plugin;
    }
    
    
    public boolean isOurInventory(Inventory inv){
        
    }
    
    private void rebuildInventories(Map<UUID, RaffleData> raffles){
        int pages = (int) Math.ceil(raffles.size() / 5d);
        
        if (pages == 0){
            pages = 1;
        }
    }
    
    public InventoryWrapper getRaffleInventory(RaffleData data){
        if (this.inventories == null || this.inventories.length == 0){
            this.rebuildInventories(this.plugin.getRaffleManager().getRaffles());
        }
        
        return this.dataInvMap.get(data);
    }
    
    public void removeFromList(RaffleData data){
        
    }
    
    public void addToList(RaffleData data){
        
    }
    
    public void updateListData(RaffleData data){
        
    }
}
