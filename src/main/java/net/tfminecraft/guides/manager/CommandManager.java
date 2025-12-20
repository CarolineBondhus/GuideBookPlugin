package net.tfminecraft.guides.manager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.tfminecraft.guides.Guides;

public class CommandManager implements CommandExecutor{
    public String cmd1 = "guide";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase(cmd1)){
            if(sender instanceof Player){
                Player p = (Player)sender;
                p.sendMessage("Haolo");
                Guides.getInstance().getGuideManager().openBook(p);
            }
        }
        return true;
    }


}
