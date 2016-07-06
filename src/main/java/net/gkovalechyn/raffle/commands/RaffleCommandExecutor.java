/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.raffle.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.gkovalechyn.raffle.Message;
import net.gkovalechyn.raffle.Raffle;
import net.gkovalechyn.raffle.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

/**
 *
 * @author gkovalechyn
 */
public class RaffleCommandExecutor implements CommandExecutor{
    private final Raffle plugin;
    private final Map<String, ICommand> commands = new HashMap<>();

    public RaffleCommandExecutor(Raffle plugin) {
        this.plugin = plugin;
    }
    
    
    //<raffle/sorteio> Create <tickets> <price>
    //<raffle/sorteio> Cancel
    //<raffle/sorteio> Buy <Player> <Amount>
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args2) {
        String[] args = this.fixArguments(args2);
        if (args.length == 0) {
            StringBuilder builder = new StringBuilder(64);
            
            sender.sendMessage(Message.GENERAL_HEADER.getText());
            for (Map.Entry<String, ICommand> entry : this.commands.entrySet()) {
                builder.append("・").append(entry.getKey()).append(" - ").append(entry.getValue().getDescription());
                sender.sendMessage(builder.toString());
                builder.setLength(0);
            }
            sender.sendMessage(Message.GENERAL_FOOTER.getText());
            
            return true;
        } else {
            ICommand icmd = this.commands.get(args[0]);
            if (icmd != null) {
                if (!icmd.supportsConsole() && (sender instanceof ConsoleCommandSender)) {
                    sender.sendMessage(Message.ERROR_CONSOLE_NOT_SUPPORTED.getText());
                    return true;
                }

                if (!sender.hasPermission(icmd.getPermission())) {
                    sender.sendMessage(Message.ERROR_NO_PERMISSION.getTextReplaced("{Permission}", icmd.getPermission()));
                    return true;
                }

                icmd.execute(sender, args, plugin);
                return true;
            } else {
                sender.sendMessage(Message.ERROR_NO_COMMAND.getText());
                return true;
            }
        }
    }
    
    private String[] fixArguments(String[] args) {
        List<String> result = new ArrayList<>(args.length);
        char[] full = Util.joinString(args).toCharArray();
        StringBuilder builder = new StringBuilder(12);
        String[] resultArray;

        if (full.length == 0){
            return new String[0];
        }
        
        for (int i = 0; i < full.length; i++) {
            switch (full[i]) {
                case '\\':
                    i++;
                    builder.append(readEscapedCharacter(full[i]));
                    break;
                case '\"':
                    i++;

                    while (i < full.length) {
                        if (full[i] == '\"') {
                            break;
                        } else if (full[i] == '\\') {
                            i++;
                            builder.append(readEscapedCharacter(full[i]));
                        } else {
                            builder.append(full[i]);
                        }
                        i++;
                    }
                    break;
                case ' ':
                    result.add(builder.toString());
                    builder.setLength(0);
                    break;
                default:
                    builder.append(full[i]);
            }
        }
        
        result.add(builder.toString());

        resultArray = new String[result.size()];

        for (int i = 0; i < resultArray.length; i++) {
            resultArray[i] = result.get(i);
        }

        return resultArray;
    }

    private char readEscapedCharacter(char c) {
        switch (c) {
            case 'n':
                return '\n';
            case '\"':
                return '\"';
            case '\'':
                return '\'';
            case 't':
                return '\t';
            case '\\':
                return '\\';
            default:
                return c;
        }
    }
    
}
