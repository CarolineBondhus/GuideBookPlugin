package net.tfminecraft.guides.guide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.tfminecraft.guides.loader.GuideLoader;
import net.tfminecraft.guides.utilities.PageCenter;
import net.tfminecraft.guides.utilities.PageParser;

public class Guide {
    private String id;
    private List<String> pages = new ArrayList<>();
    private List<String> keywords = new ArrayList<>();
    
    //legge til string med keyword og gjør samme som med
    public Guide(String key, ConfigurationSection config) {
        id = key;

        for(String s : config.getStringList("pages")){
            //Adds pages to list
            pages.add(s);
        }

        for(String s : config.getStringList("keywords")){
            keywords.add(s);
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

    public List<String> getKeywords(){
        return keywords;
    }
    
    public String getCoverPage() {
        String coverPage =
            "\n\n\n\n\n" +
            PageCenter.centerLine("§7A guide about", false)+"\n" +
            PageCenter.centerLine("§6§l" + id.toUpperCase() + "§r", true)+"\n\n";

        return coverPage;
    }
    
    public List<BaseComponent[]> getParsedPages(){
        return PageParser.getParsedPages(this);
    }


}
