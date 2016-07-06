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

/**
 *
 * @author gkovalechyn
 */
public class RaffleManager implements YamlSerializable{
    private final Map<UUID, RaffleData> raffles = new HashMap<>();

    public boolean hasRaffle(UUID player){
        return this.raffles.containsKey(player);
    }
    
    public void putRaffle(UUID owner, RaffleData data){
        this.raffles.put(owner, data);
    }
    
    public Map<UUID, RaffleData> getRaffles() {
        return new HashMap<>(raffles);
    }
    
    public void cancelRaffle(UUID owner){
        
    }
    
    public void runRaffle(UUID owner){
        
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
