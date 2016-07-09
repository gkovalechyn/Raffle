
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
 * Created on 09/07/2016, 15:54:10
 */
public class BuyButton extends DisplayButton{

    public BuyButton(ItemStack item) {
        super(item);
    }
    
    @Override
    public void onClicked(InventoryWrapper inv, Player player, Raffle plugin){
        
    }

}
