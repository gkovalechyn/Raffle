/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.raffle;

import java.io.File;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author gkovalechyn
 */
public enum Message {
    GENERAL_HEADER("General.Header"),
    GENERAL_FOOTER("General.Footer"),
    GENERAL_WIN("General.Win"),
    GENERAL_GLOBAL_WIN("General.GlobalWin"),
    
    ERROR_NO_COMMAND("Error.NoSuchCommand"),
    ERROR_CONSOLE_NOT_SUPPORTED("Error.ConsoleNotSupported"),
    ERROR_NO_PERMISSION("Error.NoPermission"),
    ERROR_NO_MONEY("Error.NotEnoughMoney"),
    ERROR_NO_RAFFLE_SELF("Error.NoRaffleSelf"),
    ERROR_NO_RAFFLE_OTHER("Error.NoRaffleOther"),
    ERROR_INVENTORY_FULL("Error.InventoryFull"),
    
    CMD_CREATE_DESC("Commands.Create.Description"),
    CMD_CREATE_USAGE("Commands.Create.Usage"),
    CMD_CREATE_CREATED("Commands.Create.Created"),
    CMD_CREATE_ALREADY_EXISTS("Commands.Create.AlreadyExists"),
    CMD_CREATE_NO_ITEM("Commands.Create.NoItem"),
    CMD_CREATE_ITEM_BLOCKED("Commands.Create.ItemBlocked"),
    
    CMD_CANCEL_DESC("Commands.Cancel.Description"),
    CMD_CANCEL_USAGE("Commands.Cancel.Usage"),
    CMD_CANCEL_CANCELLED("Commands.Cancel.Cancelled"),
    
    CMD_BUY_DESC("Commands.Buy.Description"),
    CMD_BUY_USAGE("Commands.Buy.Usage"),
    CMD_BUY_CANTBUY("Commands.Buy.CantBuy"),
    CMD_BUY_CANTBUYAMOUNT("Commands.Buy.CantBuyAmount"),
    CMD_BUY_BOUGHT("Commands.Buy.Bought"),
    
    CMD_LIST_DESC("Commands.List.Description"),
    CMD_LIST_FORMAT("Commands.List.Format"),
    
    CMD_INFO_DESC("Commands.Info.Description"),
    
    INV_NAME("Inventory.Name"),
    INV_NEXT("Inventory.Next"),
    INV_PREVIOUS("Inventory.Previous"),
    INV_BUY("Inventory.Buy"),
    
    DUMMY("null");
    
    private static FileConfiguration config;
    private static char colorCodeCharacter = '&';
    private final String path;

    private Message(String path) {
        this.path = path;
    }
    
    public static void setMessagesFile(File file){
        if (file.exists()){
            config = YamlConfiguration.loadConfiguration(file);
            colorCodeCharacter = config.getString("ColorCharacter", "&").charAt(0);
        }
    }
    

    public String getPath() {
        return path;
    }
    
    public String getText(){
        return ChatColor.translateAlternateColorCodes(colorCodeCharacter, config.getString(this.path));
    }
    
    public String getTextReplaced(String... args){
        if (args.length % 2 != 0){
            //@TODO Throw a decent exception
            throw new RuntimeException();
        }
        
        String result = this.getText();
        
        for(int i = 0; i < args.length; i += 2){
            result = result.replace((CharSequence) args[i], (CharSequence) args[i + 1]);
        }
            
        return result;
    }
    
}
