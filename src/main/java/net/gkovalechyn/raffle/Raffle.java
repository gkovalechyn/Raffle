/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.raffle;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import net.gkovalechyn.raffle.commands.FlexibleCommand;
import net.gkovalechyn.raffle.commands.RaffleCommandExecutor;
import net.gkovalechyn.raffle.util.IdDataWrapper;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author gkovalechyn
 */
public class Raffle extends JavaPlugin{
    private static final String VERSION = "0.1";
    
    private RaffleManager raffleManager;
    
    private final List<IdDataWrapper> blockedItems = new ArrayList<>();
    
    private Economy economy;
    
    //Yes, I'm lazy
    private double cost = 0;
    
    private RaffleWorker worker;
    
    @Override
    public void onEnable(){
        if (!this.setupEconomy()){
            this.getLogger().severe("Could not find vault!");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        RaffleCommandExecutor rce;
        this.saveDefaultConfig();
        this.reloadConfig();
        
        this.loadMessagesFile();
        
        this.raffleManager = new RaffleManager(this);
        this.worker = new RaffleWorker(this);
        
        rce = new RaffleCommandExecutor(this);
        this.registerCustomCommand(this.getConfig().getString("Options.BaseCommand"), new FlexibleCommand(this.getConfig().getString("Options.BaseCommand"), rce));
        
        this.load();
        
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, worker, 20 * 30, 20 * 30);
    }
    
    @Override
    public void onDisable(){
        this.save();
    }

    public RaffleManager getRaffleManager() {
        return raffleManager;
    }

    public Economy getEconomy() {
        return economy;
    }

    public double getCost() {
        return cost;
    }

    public RaffleWorker getWorker() {
        return worker;
    }
    
    private void load(){
        File dataFile = new File(this.getDataFolder(), "Data.yml");
        
        if (dataFile.exists()){
            FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
            
            if (!VERSION.equals(config.getString("Version"))){
                this.getLogger().warning("The data file version is different than the one from the plugin.");
                this.getLogger().warning("Trying to load the data file anyway.");
            }
            
            this.raffleManager.deserialize(config.getConfigurationSection("RaffleManager"));
            this.worker.deserialize(config.getConfigurationSection("RaffleWorker"));
        }
    }
    
    public void save(){
        File dataFile = new File(this.getDataFolder(), "Data.yml");
        FileConfiguration config = new YamlConfiguration();
        
        config.set("Version", VERSION);
        
        this.raffleManager.serialize(config.createSection("RaffleManager"));
        this.worker.serialize(config.createSection("RaffleWorker"));
        
        try{
            config.save(dataFile);
        }catch (IOException e){
            this.getLogger().severe("COULD NOT SAVE RAFFLE DATA!");
            e.printStackTrace();
        }
    }
    
    private void loadMessagesFile(){
        String file = this.getConfig().getString("Options.MessagesFile");
        File f = new File(this.getDataFolder(), file);
        
        if (!f.exists()){
            this.getLogger().severe("The message file in the configuration does not exist.");
            File defaultFile = new File(this.getDataFolder(), "Messages.yml");
            
            if (!(defaultFile).exists()){
                this.saveResource("Messages.yml", true);
            }
            
            Message.setMessagesFile(defaultFile);
        }else{
            Message.setMessagesFile(f);
        }
    }
    
    public boolean isItemBlocked(int itemID, byte data){
        for(IdDataWrapper wrapper : this.blockedItems){
            if (itemID == wrapper.id){
                if (wrapper.data == 0){
                    return true;
                }else if (wrapper.data == data){
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private void registerCustomCommand(String command, FlexibleCommand fc){
        try {
            //Hope this works
            Field commandMapField = this.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap cm = (CommandMap) commandMapField.get(this.getServer());
            cm.register(command, fc);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private boolean setupEconomy(){
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
    
}
