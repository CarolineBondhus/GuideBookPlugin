package net.tfminecraft.guides.guide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

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
            PageCenter.centerLine("§7A guide about", false)+"\n" +
            PageCenter.centerLine("§6§l" + id.toUpperCase() + "§r", true)+"\n\n";

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
            .append(PageCenter.centerLine("§6§lContents:§r§0", true))
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
                    .append(PageCenter.centerLine("§6§lContents:§r§0", true))
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

        //Try one line
        int oneLineWidth = getPixelWidth(title, bold) + pageWidth;
        if (oneLineWidth < LINE_WIDTH) {
            int dots = (LINE_WIDTH - oneLineWidth) / dotPx;
            return title + ".".repeat(dots) + pageStr;
        }

        //Two lines : build line 2 first
        int maxTextWidthLine2 =
                LINE_WIDTH - pageWidth - (MIN_DOTS * dotPx);

        String[] words = title.split(" ");
        String line2 = "";
        int line2Width = 0;

        //Fill the last line from the back
        for (int i = words.length - 1; i >= 0; i--) {
            int w = getPixelWidth(words[i] + " ", bold);
            if (line2Width + w <= maxTextWidthLine2) {
                line2 = words[i] + " " + line2;
                line2Width += w;
            } else {
                break;
            }
        }

        line2 = line2.trim();

        //The rest is line 1
        String line1 = title.substring(0,
                title.length() - line2.length()).trim();

        int dots = (LINE_WIDTH - getPixelWidth(line2, bold) - pageWidth) / dotPx;

        return line1 + "\n" + line2 + ".".repeat(dots) + pageStr;
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
