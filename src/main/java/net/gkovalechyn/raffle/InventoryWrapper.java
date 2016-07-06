/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.raffle;

import java.util.UUID;
import org.bukkit.inventory.Inventory;

/**
 *
 * @author gkovalechyn
 */
public class InventoryWrapper {
    private final Inventory inventory;
    private final int page;
    
    private final UUID[] inventoryPlayers = new UUID[5];
    
    public InventoryWrapper(Inventory inventory, int page) {
        this.inventory = inventory;
        this.page = page;
    }
    
    public void handleClick(int slot){
        
    }
    
    public void updatePlayerRaffle(UUID player, int totalTickets, int soldTickets){
        
    }
    
    public void addPlayerRaffle(UUID player, int totalTickets, int soldTickets){
        
    }
}
