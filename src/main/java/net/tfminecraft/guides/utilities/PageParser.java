package net.tfminecraft.guides.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.tfminecraft.guides.guide.Guide;
import net.tfminecraft.guides.loader.GuideLoader;

public class PageParser {
    public static List<BaseComponent[]> getParsedPages(Guide guide){
        List<BaseComponent[]> result = new ArrayList<>();

        result.add(TextComponent.fromLegacyText(guide.getCoverPage()));

        HashMap<String,String> keywordMap = new HashMap<>();

        for(Guide g : GuideLoader.get()){
            if(g.getId().equalsIgnoreCase(guide.getId())) continue;
            for(String keyword : g.getKeywords()){
                keywordMap.put(keyword.toLowerCase(), g.getId());
            }
        }


        for(String page : guide.getPages()){

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
