package net.tfminecraft.guides.manager;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.tfminecraft.guides.Guides;
import net.tfminecraft.guides.guide.Guide;
import net.tfminecraft.guides.loader.GuideLoader;

//Implements CommandExecutor from bukkit
public class CommandManager implements CommandExecutor{

    //Command name /guide
    public String cmd1 = "guide";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //Checks if sender is a player
        if(sender instanceof Player){
            //Convert the sender to a player object
            Player p = (Player)sender;
            //Checks if commando = /guide
            if(cmd.getName().equalsIgnoreCase(cmd1)){
                if(args.length > 0) {
                    String guideId = args[0];

                    if(guideId.equalsIgnoreCase("create")){
                        ItemStack item = p.getInventory().getItemInMainHand();

                        if(item.getType().equals(Material.WRITTEN_BOOK)){
                            GuideLoader.create(p, item);
                            return true;
                        } else {
                            p.sendMessage("§cYou must be holding a written book to create a guide.");
                            return true;
                        }
                    }

                    if(guideId.equalsIgnoreCase("edit") && args.length > 1 ){
                        guideId = args[1];
                        //Finds a guide based on ID
                        Guide guide = GuideLoader.getByString(guideId);
                        Guides.getInstance().getGuideManager().editBook(p, guide);
                        return true;
                    }

                    if(guideId.equalsIgnoreCase("save")){
                        ItemStack item = p.getInventory().getItemInMainHand();

                        if(item.getType().equals(Material.WRITABLE_BOOK)){
                            Guides.getInstance().getGuideManager().saveEditBook(p, item);
                            return true;
                        }
                    }
                    Guide guide = GuideLoader.getByString(guideId);
                    if(guide == null){
                        p.sendMessage("§cNo guide with that name exists.");
                        return true;
                    }
                    //Get the plugin → get the guide manager → open the guide book
                    Guides.getInstance().getGuideManager().openBook(p, guide);
                    return true;
                } else {
                    p.sendMessage("§cNo guide specified.");
                    return true;
                }
            }
        }
        return true;
    }


}
