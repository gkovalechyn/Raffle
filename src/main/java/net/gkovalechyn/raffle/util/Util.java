/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.raffle.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

/**
 *
 * @author gkovalechyn
 */
public class Util {

    private static final StringBuilder STRING_BUILDER = new StringBuilder(64);
    private static final Pattern TIME_PATTERN = Pattern.compile("\\d{1,}[mhdwMys]");

    public static String joinString(String[] array) {
        Util.STRING_BUILDER.setLength(0);

        for (int i = 0; i < array.length; i++) {
            Util.STRING_BUILDER.append(array[i]);
            if (i < (array.length - 1)) {
                Util.STRING_BUILDER.append(' ');
            }
        }

        return Util.STRING_BUILDER.toString();
    }

    public static String escapeString(String orig) {
        return orig.replaceAll("\n", "\\n").replaceAll("\t", "\\t");
    }

    public static String unescapeString(String orig) {
        return orig.replaceAll("\\n", "\n").replaceAll("\\t", "\t");
    }

    public static String timespanToString(long l) {
        long days = 0;
        long hours = 0;
        long minutes = 0;
        long seconds = 0;

        if (l > 86400000l){
            long div = l / 86400000l;
            days += div;
            l -= 86400000l * div;
        }
        
        if (l > 3600000l){
            long div = l / 3600000l;
            hours += div;
            l -= 3600000l * div;
        }
        
        if (l > 60000l){
            long div = l / 60000l;
            minutes += div;
            l -= 60000l * div;
        }
        
        if (l > 1000l){
            long div = l / 1000l;
            seconds += div;
            l -= 1000l * div;
        }
        
        return String.format("%02dd %02dH:%02d:%02d", days, hours, minutes, seconds);
    }

    public static int LevenshteinDistance(CharSequence lhs, CharSequence rhs) {
        int len0 = lhs.length() + 1;
        int len1 = rhs.length() + 1;

        // the array of distances                                                       
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        // initial cost of skipping prefix in String s0                                 
        for (int i = 0; i < len0; i++) {
            cost[i] = i;
        }

        // dynamically computing the array of distances                                  
        // transformation cost for each letter in s1                                    
        for (int j = 1; j < len1; j++) {
            // initial cost of skipping prefix in String s1                             
            newcost[0] = j;

            // transformation cost for each letter in s0                                
            for (int i = 1; i < len0; i++) {
                // matching current letters in both strings                             
                int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation                               
                int cost_replace = cost[i - 1] + match;
                int cost_insert = cost[i] + 1;
                int cost_delete = newcost[i - 1] + 1;

                // keep minimum cost                                                    
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }

            // swap cost/newcost arrays                                                 
            int[] swap = cost;
            cost = newcost;
            newcost = swap;
        }

        // the distance is the cost for transforming all letters in both strings        
        return cost[len0 - 1];
    }

    public static long timeStringToLong(String string) {
        Validate.notNull(string);
        long res = 0l;
        Matcher mat = Util.TIME_PATTERN.matcher(string);

        while (mat.find()) {
            String a = mat.group();
            long l = Long.parseLong(a.substring(0, a.length() - 1));

            switch (a.charAt(a.length() - 1)) {
                case 's':
                    l *= 1000L;
                    break;
                case 'm': //minutes
                    l *= 60000l;
                    break;
                case 'h': //hours
                    l *= 3600000l;
                    break;
                case 'd': //days
                    l *= 86400000l;
                    break;
                case 'w': //weeks
                    l *= 604800000l;
                    break;
                case 'M': //months
                    l *= 2419200000l;
                    break;
                case 'y': //years
                    l *= 29030400000l;
                    break;
            }
            res += l;
        }
        return res;
    }

    public static void serializeItem(ConfigurationSection section, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        
        section.set("ID", item.getTypeId());
        section.set("Data", item.getData().getData());
        section.set("Amount", item.getAmount());
        section.set("Durability", item.getDurability());
        
        if (meta != null){
            ConfigurationSection metaSection = section.createSection("Meta");
            List<String> enchantments = new ArrayList<>(4);
            
            metaSection.set("Name", meta.getDisplayName());
            metaSection.set("Lore", meta.getLore());
            
            for(Map.Entry<Enchantment, Integer> entry : meta.getEnchants().entrySet()){
                enchantments.add(entry.getKey().getName() + "," + entry.getValue());
            }
            
            metaSection.set("Enchantments", enchantments);
        }
    }

    public static ItemStack deserializeItem(ConfigurationSection section) {
        ItemStack result = new ItemStack(section.getInt("ID"));
        ConfigurationSection metaSection = section.getConfigurationSection("Meta");
        
        result.setData(new MaterialData(section.getInt("ID"), (byte) section.getInt("Data")));
        result.setDurability((short) section.getInt("Durability"));
        result.setAmount(section.getInt("Amount"));
        
        if (metaSection != null){
            ItemMeta meta = result.getItemMeta();
            
            if (meta == null){
                meta = Bukkit.getItemFactory().getItemMeta(result.getType());
            }
            
            meta.setDisplayName(metaSection.getString("Name"));
            meta.setLore(metaSection.getStringList("Lore"));
            
            for(String s : metaSection.getStringList("Enchantments")){
                String[] temp = s.split(",");
                meta.addEnchant(Enchantment.getByName(temp[1]), Integer.parseInt(temp[1]), true);
            }
            
            result.setItemMeta(meta);
        }
        
        return result;
    }
}
