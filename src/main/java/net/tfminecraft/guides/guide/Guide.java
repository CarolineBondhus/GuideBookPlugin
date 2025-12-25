package net.tfminecraft.guides.guide;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

public class Guide {
    private String id;
    private List<String> pages = new ArrayList<>();
    
    //legge til string med keyword og gjør samme som med
    public Guide(String key, ConfigurationSection config) {
        id = key;
        for(String s : config.getStringList("pages")){
            //Adds pages to list
            pages.add(s);
        }
    }

    public String getId() {
        return id;
    }

    public List<String> getPages(){
        return pages;
    }

    public void setPages(List<String> pages){
        this.pages = new ArrayList<>(pages);
    }
}
