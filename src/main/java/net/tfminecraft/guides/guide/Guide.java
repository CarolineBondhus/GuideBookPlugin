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
        List<BaseComponent[]> result = new ArrayList<>();

        result.add(TextComponent.fromLegacyText(getCoverPage()));

        HashMap<String,String> keywordMap = new HashMap<>();

        for(Guide guide : GuideLoader.get()){
            if(guide.getId().equalsIgnoreCase(id)) continue;
            for(String keyword : guide.getKeywords()){
                keywordMap.put(keyword.toLowerCase(), guide.getId());
            }
        }


        for(String page : pages){

            List<BaseComponent> components = new ArrayList<>();

            //The text we did not yet 
            String remaining = page;

            while (true) {

                String foundKeyword = null;
                int foundIndex = -1;

                // Find the earliest keyword in the remaining text
                for (String keyword : keywordMap.keySet()) {
                    int index = remaining.toLowerCase().indexOf(keyword);

                    if (index != -1 && (foundIndex == -1 || index < foundIndex)) {
                        foundIndex = index;
                        foundKeyword = keyword;
                    }
                }

                // No more keywords found
                if (foundKeyword == null) break;

                // Text before keyword
                if (foundIndex > 0) {
                    components.add(
                        new TextComponent(remaining.substring(0, foundIndex))
                    );
                }

                // Clickable keyword
                TextComponent clickable = new TextComponent(
                    remaining.substring(foundIndex, foundIndex + foundKeyword.length())
                );
                clickable.setColor(ChatColor.BLUE);
                clickable.setClickEvent(
                    new ClickEvent(
                        ClickEvent.Action.RUN_COMMAND,
                        "/guide " + keywordMap.get(foundKeyword)
                    )
                );

                components.add(clickable);

                // Move forward
                remaining = remaining.substring(foundIndex + foundKeyword.length());
            }

            // Remaining text
            if (!remaining.isEmpty()) {
                components.add(new TextComponent(remaining));
            }

            result.add(components.toArray(new BaseComponent[0]));
        }

        return result;
    }


}
