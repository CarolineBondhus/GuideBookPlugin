package net.tfminecraft.guides.loader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.tfminecraft.guides.guide.Guide;

public class GuideLoader {
    public static List<Guide> oList = new ArrayList<>();
    public static List<Guide> get(){
        return oList;
    }
    public static Guide getByString(String id) {
        for(Guide r : oList) {
            if(r.getId().equalsIgnoreCase(id)) return r;
        }
        return null;
    }
    public void load(File configFile) {
        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        Set<String> set = config.getKeys(false);

        List<String> list = new ArrayList<String>(set);

        for(String key : list) {
            Guide r = new Guide(key, config.getConfigurationSection(key));
            oList.add(r);
        }
    }
}
