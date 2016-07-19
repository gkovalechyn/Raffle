
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.gkovalechyn.raffle.util;

import org.bukkit.configuration.ConfigurationSection;

/**
 * @author gkovalechyn
 * Created on 16/07/2016, 01:20:48
 */
public class ConfigurationUpdater {
    public static void updateConfiguration(ConfigurationSection toBeUpdated, ConfigurationSection toCompare){
        updateConfiguration(toBeUpdated, toCompare, "");
    }
    
    public static void updateConfiguration(ConfigurationSection toBeUpdated, ConfigurationSection toCompare, String nodePath){
        for(String node : toCompare.getKeys(false)){
            String fullNodePath = nodePath + "." + node;
            Object o = toBeUpdated.get(fullNodePath);
            
            if (o instanceof ConfigurationSection){
                updateConfiguration(toBeUpdated, toCompare, fullNodePath);
            }else if (!toBeUpdated.contains(fullNodePath)){
                toBeUpdated.set(fullNodePath, toCompare.get(fullNodePath));
            }
        }
    }
}
