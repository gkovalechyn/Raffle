
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
    private final Map<RaffleData, InventoryWrapper> dataInvMap = new HashMap<>();
    private final Raffle plugin;
    
    public InventoryManager(Raffle plugin) {
        this.plugin = plugin;
    }
    
    
    public boolean isOurInventory(Inventory inv){
        if (this.inventories != null){
            for(InventoryWrapper wrapper : this.inventories){
                if (wrapper.getInv().equals(inv)){
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public InventoryWrapper getInventoryByPage(int page){
        if (this.inventories == null){
            this.rebuildInventories(this.plugin.getRaffleManager().getRaffles());
        }
        
        if (page > 0){
            return this.inventories[page % this.inventories.length];
        }else{
            return this.inventories[0];
        }
    }
    
    public void rebuildInventories(){
        this.rebuildInventories(plugin.getRaffleManager().getRaffles());
    }
    
    private void rebuildInventories(Map<UUID, RaffleData> raffles){
        int pages = (int) Math.ceil(raffles.size() / 5d);
        int currentPage = 0;
        int currentLine = 0;
        
        if (pages == 0){
            pages = 1;
        }
        
        this.dataInvMap.clear();
        this.inventories = new InventoryWrapper[pages];
        
        for(Map.Entry<UUID, RaffleData> entry : raffles.entrySet()){
            this.inventories[currentPage].setLine(currentLine, entry.getValue());
            this.dataInvMap.put(entry.getValue(), this.inventories[currentPage]);
            
            currentLine++;
            if (currentLine > 5){
                currentLine = 0;
                currentPage++;
            }
        }
    }
    
    public InventoryWrapper getRaffleInventory(RaffleData data){
        if (this.inventories == null){
            this.rebuildInventories(this.plugin.getRaffleManager().getRaffles());
        }
        
        return this.dataInvMap.get(data);
    }
    
    public void updateListData(RaffleData data){
        InventoryWrapper wrapper = this.dataInvMap.get(data);
        
        if (wrapper != null){
            wrapper.updateData(data);
        }
    }
}
