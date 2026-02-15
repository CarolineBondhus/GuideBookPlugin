package net.tfminecraft.guides.guide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import me.Plugins.TLibs.Objects.API.SubAPI.StringFormatter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.tfminecraft.guides.enums.CharWidth;
import net.tfminecraft.guides.utilities.PageCenter;
import net.tfminecraft.guides.utilities.PageParser;

public class Guide {
    private String id;
    private List<String> pages = new ArrayList<>();
    private List<String> keywords = new ArrayList<>();
    private Map<String, Integer> tags = new HashMap<>();
    private static final int MAX_LINES_PER_PAGE = 14;
    private static final String textColor = "#7a583c";

    
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

    public Map<String, Integer> getTags(){
        return tags;
    }
    
    public void addTag(String text, int index){
        tags.put(text, index);
    }

    public void addKeyword(String keyword){
        if(!keywords.contains(keyword)){
            keywords.add(keyword);
        }
    }
    
    public void clearTags() {
        tags.clear();
    }
    public String getCoverPage() {
        String coverPage =
            "\n\n\n\n\n" +
            PageCenter.centerLine(StringFormatter.formatHex(textColor +"A guide about" +"\n"), false) +
            PageCenter.centerLine(StringFormatter.formatHex(textColor + "§l" + id.toUpperCase() + "§r"), true)+"\n\n";

        return coverPage;
    }

    public List<String> getContentsPages(){
        //Build contents page to get size
        int contentsPageCount = buildContentsPages(0).size();

        //Set how many pages come before the content itself
        int pageOffset = 2 + contentsPageCount;

        //Build again with correct number
        return buildContentsPages(pageOffset);

    }

    private List<String> buildContentsPages(int pageOffset){
        // Sort chapters
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(tags.entrySet());
        entries.sort(
            Map.Entry.<String, Integer>comparingByValue()
                .thenComparing(Map.Entry.comparingByKey(String.CASE_INSENSITIVE_ORDER))
        );

        List<String> result = new ArrayList<>();

        StringBuilder currentPage = new StringBuilder();
        int lineCount = 0;

        // Start first page
        currentPage.append("\n")
            .append(PageCenter.centerLine(StringFormatter.formatHex(textColor +"§lContents:§r"), true))
            .append("\n\n");

        lineCount = 2; //title + blank line;


        for(Map.Entry<String, Integer> entry : entries){
            String title = entry.getKey();
            int page = entry.getValue() + pageOffset;

            //Each line in contents gets dots before the pagenumber
            String formatted = formatContentsLine(title, page);

            //Counts how many lines the book uses
            int linesUsed = formatted.split("\n").length;

            //If page is full then make new page
            if(lineCount + linesUsed > MAX_LINES_PER_PAGE-2){
                result.add(currentPage.toString());

                currentPage = new StringBuilder();
                currentPage.append("\n")
                    .append(PageCenter.centerLine(StringFormatter.formatHex(textColor +"§lContents:§r"), true))
                    .append("\n\n");

                lineCount = 2;
            }

            currentPage.append(formatted).append("\n");
            lineCount += linesUsed;

        }

        //Add last page
        if(currentPage.length() > 0){
            result.add(currentPage.toString());
        }

        System.out.println("lines counted: " + lineCount);

        return result;
    }
    
    private String formatContentsLine(String title, int page) {
        final int LINE_WIDTH = 114;
        final int MIN_DOTS = 3;
        boolean bold = false;

        String pageStr = String.valueOf(page);
        int pageWidth = getPixelWidth(pageStr, bold);
        int dotPx = CharWidth.getWidth('.', bold);

        int sidePaddingpx = CharWidth.getWidth(' ', bold) * 2; //2 spaces
        int usableWidth = LINE_WIDTH - (sidePaddingpx * 2);

        List<String> wrapped = wrapTitle(title, usableWidth, bold);

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < wrapped.size(); i++) {
            String line = wrapped.get(i);

            // Last line gets dots + page number
            if (i == wrapped.size() - 1) {
                int dots = Math.max(0,
                    (usableWidth - getPixelWidth(line, bold) - pageWidth) / dotPx
                );

                result.append(textColor)
                    .append("  ")
                    .append(line)
                    .append(".".repeat(dots))
                    .append(pageStr);
            } else {
                result.append(textColor)
                    .append("  ")
                    .append(line);
            }

            result.append("\n");
        }

        return StringFormatter.formatHex(result.toString().trim());
    }


    private List<String> wrapTitle(String title, int maxWidth, boolean bold) {
        List<String> lines = new ArrayList<>();
        String[] words = title.split(" ");

        StringBuilder currentLine = new StringBuilder();
        int currentWidth = 0;

        for (String word : words) {
            int wordWidth = getPixelWidth(word + " ", bold);

            if (currentWidth + wordWidth > maxWidth) {
                lines.add(currentLine.toString().trim());
                currentLine = new StringBuilder();
                currentWidth = 0;
            }

            currentLine.append(word).append(" ");
            currentWidth += wordWidth;
        }

        if (!currentLine.isEmpty()) {
            lines.add(currentLine.toString().trim());
        }

        return lines;
    }



    private int getPixelWidth(String text, boolean bold) {
        String stripped = text.replaceAll("§.", "");
        int width = 0;

        for (char c : stripped.toCharArray()) {
            width += CharWidth.getWidth(c, bold);
        }

        return width;
    }



    public List<BaseComponent[]> getParsedPages(){
        return PageParser.getParsedPages(this);
    }


}
