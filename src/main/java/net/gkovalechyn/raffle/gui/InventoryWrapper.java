
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.gkovalechyn.raffle.gui;

import java.util.HashMap;
import java.util.Map;
import net.gkovalechyn.raffle.Message;
import net.gkovalechyn.raffle.Raffle;
import net.gkovalechyn.raffle.RaffleData;
import net.gkovalechyn.raffle.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author gkovalechyn
 * Created on 09/07/2016, 15:32:06
 */
public class InventoryWrapper {
    private final Inventory inv;
    private final Map<Integer, InventoryButton> buttons =  new HashMap<>();
    private final RaffleData[] raffles = new RaffleData[5];
    
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
    
    public void setLine(int line, RaffleData data){
        if (line < 6){ //Line 6 is the line for the control buttons
            int index = line * 9;
            //If we don't have ht head item we need to set all the buttons
            if (!this.buttons.containsKey(index)){
                ItemStack head = new ItemStack(Material.SKULL_ITEM, 1);
                ItemStack ticketDisplay = new ItemStack(Material.PAPER);
                ItemMeta displayMeta = ticketDisplay.getItemMeta();
                
                Util.setSkullHeadMeta(head, data.getOwnerName());
                
                this.setButton(index, new DisplayButton(head));
                this.setButton(index + 1, new DisplayButton(data.getItem()));
                
                ticketDisplay.setAmount(Math.min(64, data.getAvailableTicketCount()));
                
                if (displayMeta == null){
                    displayMeta = Bukkit.getItemFactory().getItemMeta(ticketDisplay.getType());
                }
                
                displayMeta.setDisplayName(Message.INV_TICKETS.getTextReplaced("{Tickets}", "" + data.getAvailableTicketCount()));
                ticketDisplay.setItemMeta(displayMeta);
                
                for(int i = index + 5; i < index + 9; i++){
                    int amount = this.getBuyAmount(i - (index - 5));
                    ItemStack item = new ItemStack(Material.GOLD_INGOT, amount);
                    ItemMeta itemMeta = item.getItemMeta();
                    
                    if (itemMeta == null) {
                        itemMeta = Bukkit.getItemFactory().getItemMeta(item.getType());
                    }
                    
                    itemMeta.setDisplayName(Message.INV_BUY.getText());
                    
                    item.setItemMeta(itemMeta);
                    
                    this.setButton(index, new BuyButton(item, data));
                }
            }else{
                InventoryButton displayButton = this.buttons.get(index + 3);
                ItemMeta meta = displayButton.getRepresentingItem().getItemMeta();
                
                meta.setDisplayName(Message.INV_TICKETS.getTextReplaced("{Tickets}", "" + data.getAvailableTicketCount()));
                
                displayButton.getRepresentingItem().setItemMeta(meta);
            }
        }
    }
    
    private int getBuyAmount(int offset){
        switch(offset){
            case 0:
                return 1;
            case 1:
                return 5;
            case 2:
                return 10;
            case 3:
                return 25;
            default:
                return 64; //We know something is wrong
        }
    }
    
    public void updateData(RaffleData raffle){
        for(int i = 0; i < this.raffles.length; i++){
            if (this.raffles[i] != null && this.raffles[i] == raffle){
                this.setLine(i, raffle);
                break;
            }
        }
    }
    
    public void updateLine(int line){
        line = line < 0 ? 0 : line % 5;
        if (this.raffles[line] != null){
            this.setLine(line, this.raffles[line]);
        }
    }

    public Inventory getInv() {
        return inv;
    }
}
