package net.tfminecraft.guides;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.tfminecraft.guides.loader.GuideLoader;
import net.tfminecraft.guides.manager.CommandManager;
import net.tfminecraft.guides.manager.GuideManager;

public class Guides extends JavaPlugin{
    public static Guides plugin;
    private final CommandManager commands = new CommandManager();
    private final GuideManager guideManager = new GuideManager();
    private final GuideLoader guideLoader = new GuideLoader();
 
    @Override 
    public void onEnable() {
        plugin = this;
        createFolders();
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage("Halo");
        }
        File f = new File(getDataFolder(), "guides");

        for(final File guide : f.listFiles()){
            if(!guide.isDirectory()){
                guideLoader.load(guide);
            }
        }
        getCommand(commands.cmd1).setExecutor(commands);


    }

    public static Guides getInstance() {
        return plugin;
    }

    public GuideManager getGuideManager() {
        return guideManager;
    }

    public void createFolders() {
        if(!getDataFolder().exists()){
            getDataFolder().mkdir();
        }

        File f = new File(getDataFolder(), "guides");

        if(!f.exists()){
            f.mkdir();
        }
    }


}
