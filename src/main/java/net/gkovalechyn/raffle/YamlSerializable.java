/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.raffle;

import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author gkovalechyn
 */
public interface YamlSerializable {
    public void serialize(ConfigurationSection fc);
    
    public void deserialize(ConfigurationSection fc);
}
