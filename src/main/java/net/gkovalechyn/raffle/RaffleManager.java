/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.raffle;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 *
 * @author gkovalechyn
 */
public class RaffleManager implements YamlSerializable {

    private final Raffle plugin;
    private final Map<UUID, RaffleData> raffles = new HashMap<>();

    public RaffleManager(Raffle plugin) {
        this.plugin = plugin;
    }

    public boolean hasRaffle(UUID player) {
        return this.raffles.containsKey(player);
    }

    public void putRaffle(UUID owner, RaffleData data) {
        this.raffles.put(owner, data);
    }

    public Map<UUID, RaffleData> getRaffles() {
        return new HashMap<>(raffles);
    }

    public void cancelRaffle(UUID owner) {
        RaffleData data = this.raffles.get(owner);

        if (data != null) {
            Player player = this.plugin.getServer().getPlayer(owner);
            this.plugin.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(owner), plugin.getCost());

            for (Map.Entry<UUID, Integer> entry : data.getBoughtTickets().entrySet()) {
                this.plugin.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(entry.getKey()), entry.getValue() * data.getPrice());
            }

            if (player != null) {
                player.sendMessage(Message.CMD_CANCEL_CANCELLED.getText());

                if (player.getInventory().firstEmpty() < 0) {
                    player.sendMessage(Message.ERROR_INVENTORY_FULL.getText());
                    plugin.getWorker().addItemToGive(owner, data.getItem());
                } else {
                    player.getInventory().addItem(data.getItem());
                }
            }else{
                plugin.getWorker().addItemToGive(owner, data.getItem());
            }
            
            plugin.getInventoryManager().rebuildInventories();
            this.raffles.remove(owner);
        }
    }

    public void runRaffle(UUID owner) {
        RaffleData data = this.raffles.get(owner);

        if (data != null) {
            Random r = new Random();
            int value = r.nextInt(data.getTicketAmount());
            int total = 0;

            for (Map.Entry<UUID, Integer> entry : data.getBoughtTickets().entrySet()) {
                total += entry.getValue();

                if (value < total) {
                    Player p = this.plugin.getServer().getPlayer(entry.getKey());
                    OfflinePlayer offlinePlayer = this.plugin.getServer().getOfflinePlayer(entry.getKey());
                    
                    if (this.plugin.isToBroadcastWin()){
                        this.plugin.getServer().broadcastMessage(Message.GENERAL_GLOBAL_WIN.getTextReplaced(
                                "{Winner}", offlinePlayer.getName(),
                                "{Player}", data.getOwnerName()
                        ));
                    }
                    
                    if (p != null) {
                        p.sendMessage(Message.GENERAL_WIN.getTextReplaced("{Player}", data.getOwnerName()));
                        
                        if (p.getInventory().firstEmpty() > 0) {
                            p.getInventory().addItem(data.getItem());
                        } else {
                            p.sendMessage(Message.ERROR_INVENTORY_FULL.getText());
                            this.plugin.getWorker().addItemToGive(entry.getKey(), data.getItem());
                        }
                    }else{
                        plugin.getWorker().addItemToGive(entry.getKey(), data.getItem());
                    }
                    break;
                }
            }

            this.plugin.getEconomy().depositPlayer(this.plugin.getServer().getOfflinePlayer(owner), data.getSoldTickets() * data.getPrice());
            this.raffles.remove(owner);
            plugin.getInventoryManager().rebuildInventories();
        }
    }

    public RaffleData getRaffle(UUID uuid) {
        return this.raffles.get(uuid);
    }

    @Override
    public void serialize(ConfigurationSection fc) {
        for (Map.Entry<UUID, RaffleData> entry : this.raffles.entrySet()) {
            entry.getValue().serialize(fc.createSection(entry.getKey().toString()));
        }
    }

    @Override
    public void deserialize(ConfigurationSection fc) {
        for (String s : fc.getKeys(false)) {
            UUID uuid = UUID.fromString(s);
            RaffleData data = new RaffleData(uuid, null, 0, 0);
            
            data.deserialize(fc.getConfigurationSection(s));

            this.raffles.put(uuid, data);
        }
    }
}
