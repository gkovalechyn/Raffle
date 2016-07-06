/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.raffle;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author gkovalechyn
 */
public class RaffleData implements YamlSerializable{
    private final UUID owner;
    private final ItemStack item;
    private final int ticketAmount;
    private final double price;
    
    private final Map<UUID, Integer> boughtTickets = new HashMap<>();
    private int soldTickets = 0;
    
    private long duration = 0l;
    
    private final long start;
    public RaffleData(UUID owner, ItemStack item, int ticketAmount, double price) {
        this(owner, item, ticketAmount, price, 0);
    }

    public RaffleData(UUID owner, ItemStack item, int ticketAmount, double price, long duration) {
        this.owner = owner;
        this.item = item;
        this.ticketAmount = ticketAmount;
        this.duration = duration;
        this.price = price;
        this.start = System.currentTimeMillis();
    }
    
    
    
    public int getPlayerBoughtTickets(UUID uuid){
        if (this.boughtTickets.containsKey(uuid)){
            return this.boughtTickets.get(uuid);
        }else{
            return 0;
        }
    }
    
    public void buyTicket(UUID player, int amount){
        if ((soldTickets + amount) <= ticketAmount){
            if (this.boughtTickets.containsKey(player)){
                this.boughtTickets.put(player, this.boughtTickets.get(player) + amount);
            }else{
                this.boughtTickets.put(player, amount);
            }
            
            this.soldTickets += amount;
        }
    }
    
    public boolean canBuyTickets(){
        return this.soldTickets < this.ticketAmount;
    }
    
    public int getSoldTickets(){
        return this.soldTickets;
    }

    public UUID getOwner() {
        return owner;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getTicketAmount() {
        return ticketAmount;
    }

    @Override
    public void serialize(ConfigurationSection fc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deserialize(ConfigurationSection fc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
