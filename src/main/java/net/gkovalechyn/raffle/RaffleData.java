/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.raffle;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.gkovalechyn.raffle.util.Util;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author gkovalechyn
 */
public class RaffleData implements YamlSerializable{
    private UUID owner;
    private ItemStack item;
    private int ticketAmount;
    private double price;
    private String ownerName;
    
    private final Map<UUID, Integer> boughtTickets = new HashMap<>();
    private int soldTickets = 0;
    
    private long duration = 0l;
    
    private long start;
    
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
    
    public boolean areTicketsAvailable(){
        return this.soldTickets < this.ticketAmount;
    }
    
    public boolean canBuyTickets(){
        return this.soldTickets < this.ticketAmount &&
                System.currentTimeMillis() < this.start + this.duration;
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public double getPrice() {
        return price;
    }

    public long getStart() {
        return start;
    }

    public Map<UUID, Integer> getBoughtTickets() {
        //Fuck it, not returning a new map
        return boughtTickets;
    }

    @Override
    public void serialize(ConfigurationSection cs) {
        ConfigurationSection buyers = cs.createSection("Buyers");
        
        cs.set("Owner", this.owner.toString());
        cs.set("OwnerName", ownerName);
        cs.set("Start", this.start);
        cs.set("Duration", this.duration);
        
        cs.set("TotalTickets", this.ticketAmount);
        cs.set("SoldTickets", this.soldTickets);
        cs.set("Price", this.price);
        
        Util.serializeItem(cs.createSection("Item"), this.item);
        
        for(Map.Entry<UUID, Integer> entry : this.boughtTickets.entrySet()){
            buyers.set(entry.getKey().toString(), entry.getValue().toString());
        }
    }

    @Override
    public void deserialize(ConfigurationSection cs) {
        ConfigurationSection buyers = cs.getConfigurationSection("Buyers");
                
        this.owner = UUID.fromString(cs.getString("Owner"));
        this.ownerName = cs.getString("OwnerName");
        this.start = cs.getLong("Start");
        this.duration = cs.getLong("Duration");
        
        this.ticketAmount = cs.getInt("TotalTickets");
        this.soldTickets = cs.getInt("SoldTickets");
        this.price = cs.getDouble("Price");
        
        this.item = Util.deserializeItem(cs.getConfigurationSection("Item"));
        
        for(String s : buyers.getKeys(false)){
            UUID uuid = UUID.fromString(s);
            this.boughtTickets.put(uuid, buyers.getInt(s));
        }
    }
    
}
