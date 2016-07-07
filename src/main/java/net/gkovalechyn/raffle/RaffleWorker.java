
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.raffle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.gkovalechyn.raffle.util.Util;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

/**
 * @author gkovalechyn Created on 07/07/2016, 17:34:19
 */
public class RaffleWorker implements Runnable, YamlSerializable {

    private final Map<UUID, List<ItemStack>> notOnlineWinners = new HashMap<>();

    private final Raffle plugin;

    private int i = 0;

    public RaffleWorker(Raffle plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        long now = System.currentTimeMillis();

        for (Map.Entry<UUID, RaffleData> entry : plugin.getRaffleManager().getRaffles().entrySet()) {
            RaffleData data = entry.getValue();

            if (!data.canBuyTickets()) {
                plugin.getRaffleManager().runRaffle(entry.getKey());
            } else if (data.getStart() + data.getDuration() < now) {
                plugin.getRaffleManager().cancelRaffle(entry.getKey());
            }
        }

        i++;

        if (i % 6 == 0) {
            this.plugin.save();
        }
    }

    @Override
    public void serialize(ConfigurationSection fc) {
        for (Map.Entry<UUID, List<ItemStack>> entry : this.notOnlineWinners.entrySet()) {
            ConfigurationSection playerSection = fc.createSection(entry.getKey().toString());
            int i = 0;
            
            for(ItemStack item : entry.getValue()){
                Util.serializeItem(playerSection.createSection("Item" + (i++)), item);
            }
        }
    }

    @Override
    public void deserialize(ConfigurationSection fc) {
        for (String stringUUID : fc.getKeys(false)) {
            UUID uuid = UUID.fromString(stringUUID);
            ConfigurationSection playerSection = fc.getConfigurationSection(stringUUID);

            for(String item : playerSection.getKeys(false)){
                this.addItemToGive(uuid, Util.deserializeItem(playerSection.getConfigurationSection(item)));
            }
        }
    }
    
    public void addItemToGive(UUID player, ItemStack item){
        List<ItemStack> list = this.notOnlineWinners.get(player);
        
        if (list == null){
            list = new ArrayList<>();
            this.notOnlineWinners.put(player, list);
        }
        
        list.add(item);
    }

}
