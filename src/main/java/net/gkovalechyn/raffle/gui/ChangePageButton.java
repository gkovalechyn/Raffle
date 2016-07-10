
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
 * Created on 11/07/2016, 00:00:11
 */
public class ChangePageButton extends DisplayButton{
    private final int pageToChangeTo;
    
    public ChangePageButton(ItemStack item, int pageToChangeTo) {
        super(item);
        this.pageToChangeTo = pageToChangeTo;
    }
    
    @Override
    public void onClicked(InventoryWrapper inv, Player player, Raffle plugin){
        player.closeInventory();
        player.openInventory(plugin.getInventoryManager().getInventoryByPage(pageToChangeTo).getInv());
    }

}
