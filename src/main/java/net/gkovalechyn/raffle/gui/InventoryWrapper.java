
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.gkovalechyn.raffle.gui;

import java.util.HashMap;
import java.util.Map;
import net.gkovalechyn.raffle.Raffle;
import net.gkovalechyn.raffle.RaffleData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author gkovalechyn
 * Created on 09/07/2016, 15:32:06
 */
public class InventoryWrapper {
    private final Inventory inv;
    private final Map<Integer, InventoryButton> buttons =  new HashMap<>();
    
    public InventoryWrapper(String title){
        this.inv = Bukkit.createInventory(null, 54, title);
    }
    
    public void handleClick(Player player, int index, Raffle plugin){
        if (this.buttons.containsKey(index)){
            this.buttons.get(index).onClicked(this, player, plugin);
        }
    }
    
    public void setButton(int index, InventoryButton button){
        if (button == null){
            this.buttons.remove(index);
            this.inv.setItem(index, new ItemStack(Material.AIR));
        }else{
            this.buttons.put(index, button);
            this.inv.setItem(index, button.getRepresentingItem());
        }   
    }
    
    public void updateLine(int line, RaffleData data){
        if (line < 6){ //Line 6 is the line for the control buttons
            int index = line * 9;
        }
    }
}
