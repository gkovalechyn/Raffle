
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.gkovalechyn.raffle.gui;

import net.gkovalechyn.raffle.Raffle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author gkovalechyn
 * Created on 09/07/2016, 15:51:48
 */
public class DisplayButton implements InventoryButton{
    private ItemStack item;

    public DisplayButton(ItemStack item) {
        this.item = item;
    }
    
    @Override
    public void onClicked(InventoryWrapper inventory, Player player, Raffle plugin) {
        //nothing
    }

    @Override
    public ItemStack getRepresentingItem() {
        return this.item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }
    
}
