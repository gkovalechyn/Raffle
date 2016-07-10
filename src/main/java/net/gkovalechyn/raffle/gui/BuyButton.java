
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.gkovalechyn.raffle.gui;

import net.gkovalechyn.raffle.Message;
import net.gkovalechyn.raffle.Raffle;
import net.gkovalechyn.raffle.RaffleData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author gkovalechyn
 * Created on 09/07/2016, 15:54:10
 */
public class BuyButton extends DisplayButton{
    private RaffleData raffle;
    public BuyButton(ItemStack item, RaffleData data) {
        super(item);
        this.raffle = data;
    }
    
    @Override
    public void onClicked(InventoryWrapper inv, Player player, Raffle plugin){
        if (this.raffle.canBuyTickets()){
            int amount = this.getRepresentingItem().getAmount();
            
            if (amount <= raffle.getAvailableTicketCount()){
                if (!plugin.getEconomy().has(player, amount * raffle.getPrice())){
                    player.sendMessage(Message.ERROR_NO_MONEY.getText());
                }else{
                    plugin.getEconomy().withdrawPlayer(player, amount * raffle.getPrice());
                    raffle.buyTicket(player.getUniqueId(), amount);
                    player.sendMessage(Message.CMD_BUY_BOUGHT.getText());
                    
                    inv.updateData(raffle);
                }
            }else{
                player.sendMessage(Message.CMD_BUY_CANTBUYAMOUNT.getText());
            }
        }else{
            player.sendMessage(Message.CMD_BUY_CANTBUY.getText());
        }
    }

    public RaffleData getRaffle() {
        return raffle;
    }

    public void setRaffle(RaffleData raffle) {
        this.raffle = raffle;
    }
}
