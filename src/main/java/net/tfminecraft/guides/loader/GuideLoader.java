package net.tfminecraft.guides.loader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import net.tfminecraft.guides.guide.Guide;

//Loads guides to RAM
public class GuideLoader {
    public static List<Guide> oList = new ArrayList<>();
    public static List<Guide> get(){
        return oList;
    }

    //Iterates through all guides and returns guide with correct id 
    public static Guide getByString(String id) {
        for(Guide r : oList) {
            if(r.getId().equalsIgnoreCase(id)) return r;
        }
        return null;
    }

    //Loads guide from file
    public void load(File configFile) {
        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        Set<String> set = config.getKeys(false);

        List<String> list = new ArrayList<String>(set);

        //Iterates through all keys in config
        for(String key : list) {
            //Chooses the section belonging to the key, and loads the guide with the key as id
            Guide r = new Guide(key, config.getConfigurationSection(key));
            oList.add(r);
        }
    }

    public static void create(Player p, ItemStack item){
        BookMeta meta = (BookMeta) item.getItemMeta();
        String title = meta.getTitle();

        //Create a guide file
        File guidesFolder = new File("plugins/Guides/guides");
        File guideFile = new File(guidesFolder, title + ".yml");

        YamlConfiguration config = new YamlConfiguration();

        //Save pages
        config.set(title + ".pages", meta.getPages());

        try {
            config.save(guideFile);
            p.sendMessage("§aGuide '" + title + "' created successfully.");
        } catch (IOException e){
            p.sendMessage("§cFailed to create guide");
            e.printStackTrace();
        }

        Set<String> set = config.getKeys(false);

        List<String> list = new ArrayList<String>(set);

        //Iterates through all keys in config
        for(String key : list) {
            //Chooses the section belonging to the key, and loads the guide with the key as id
            Guide r = new Guide(key, config.getConfigurationSection(key));
            oList.add(r);
        }
    }

    public static void save(Guide guide, Player p){
        String title = guide.getId();

        //Create a guide file
        File guidesFolder = new File("plugins/Guides/guides");
        File guideFile = new File(guidesFolder, title + ".yml");

        YamlConfiguration config =  new YamlConfiguration();

        //Save pages
        config.set(title + ".pages", guide.getPages());

        try {
            config.save(guideFile);
        } catch (IOException e){
            e.printStackTrace();
        }


    }

}
