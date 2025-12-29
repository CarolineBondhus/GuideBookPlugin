package net.tfminecraft.guides.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.tfminecraft.guides.guide.Guide;
import net.tfminecraft.guides.loader.GuideLoader;

public class PageParser {
    public static List<BaseComponent[]> getParsedPages(Guide guide){
        guide.clearTags();

        List<BaseComponent[]> result = new ArrayList<>();

        //Add coverpage
        result.add(TextComponent.fromLegacyText(guide.getCoverPage()));

        //Build keyword lookup
        HashMap<String,String> keywordMap = new HashMap<>();
        for(Guide g : GuideLoader.get()){
            if(g.getId().equalsIgnoreCase(guide.getId())) continue;
            for(String keyword : g.getKeywords()){
                keywordMap.put(keyword.toLowerCase(), g.getId());
            }
        }


        //Parse each page
        List<String> pages = guide.getPages();
        for(int pageIndex = 0; pageIndex < pages.size(); pageIndex++){
            String rawPage = pages.get(pageIndex);

            List<BaseComponent> components = new ArrayList<>();

            for(String line : rawPage.split("\n")){
                parseLine(
                    guide,
                    line, 
                    components,
                    pageIndex,
                    keywordMap
                );
            }
            result.add(components.toArray(new BaseComponent[0]));
        }
        System.out.println("Tags: " + guide.getTags());
        System.out.println("Keywords: " + guide.getKeywords());

        return result;

    }

    public static void parseLine(
        Guide guide,
        String line, 
        List<BaseComponent> components,
        int pageIndex,
        Map<String, String> keywordMap
    ){
        if(line.isBlank()){
            components.add(new TextComponent("\n"));
            return;
        }

        //<s> subtitle
        if(line.startsWith("<s>")){
            String text = line.substring(3). trim();

            guide.addTag(text, pageIndex);
            guide.addKeyword(text);

            TextComponent c = new TextComponent(text + "\n\n");
            c.setBold(true);
            c.setColor(ChatColor.GOLD);
            components.add(c);

            return;
        }

        //<ss> sub-subtitle
        if(line.startsWith("<ss>")){
            String text = line.substring(4).trim();

            guide.addTag(text, pageIndex);
            guide.addKeyword(text);

            TextComponent c = new TextComponent(text + "\n\n");
            c.setBold(true);
            c.setColor(ChatColor.DARK_GRAY);
            components.add(c);

            return;
        }

        //<k> keyword
        if(line.contains("<k>")){
            //Splits the line by all whitespace 
            String[] words = line.split("\\s+");
            StringBuilder cleanedLine = new StringBuilder();

            for(String word : words){
                if(word.startsWith("<k>")){
                    String keyword = word.substring(3).replaceAll("[^a-zA-Z0-9_-]", "");

                    if(!keyword.isEmpty()){
                        guide.addKeyword(keyword); 
                    } 
                    cleanedLine.append(keyword).append(" ");
                } else {
                    cleanedLine.append(word).append(" ");
                }
            }
                line = cleanedLine.toString().trim();
        }

        //Normal text with keyword linking
        //The text we did not yet add
            String remaining = line;

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

                String targetGuideId = keywordMap.get(foundKeyword);
                Guide targetGuide = GuideLoader.getByString(targetGuideId);

                String command;

                if (targetGuide != null && targetGuide.getTags().containsKey(foundKeyword)) {
                    int page = targetGuide.getTags().get(foundKeyword);
                    command = "/guide " + targetGuide.getId() + " " + page;
                } else {
                    command = "/guide " + targetGuideId;
                }
                clickable.setClickEvent(
                    new ClickEvent(
                        ClickEvent.Action.RUN_COMMAND,
                        command
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

            components.add(new TextComponent("\n"));
        }

    }



