package net.tfminecraft.guides.books;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import me.Plugins.TLibs.Objects.API.SubAPI.StringFormatter;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.tfminecraft.guides.guide.Guide;
import net.tfminecraft.guides.loader.GuideLoader;
import net.tfminecraft.guides.utilities.PageCenter;


public class GuideCatalogBook {

    private static final int MAX_LINES_PER_PAGE = 12;

    //Opens a catalog with all guides
    public static void open(Player p){
        BookMeta meta = (BookMeta) Bukkit.getItemFactory()
            .getItemMeta(Material.WRITTEN_BOOK);

        List<BaseComponent[]> pages = new ArrayList<>();
        String textColor = "#7a583c";

        //Cover
        pages.add(TextComponent.fromLegacyText(
            "\n\n\n\n" 
            + PageCenter.centerLine(StringFormatter.formatHex(textColor +"§lTFMC Guides"), true) 
            + "\n" + PageCenter.centerLine(StringFormatter.formatHex(textColor + "Click a guide to open it"), false)
        ));

        //Sort alphabetically
        List<Guide> guides = new ArrayList<>(GuideLoader.get());
        guides.sort((a, b) -> a.getId().compareToIgnoreCase(b.getId()));


        //Contents pages
        List<BaseComponent> currentPage = new ArrayList<>();

        currentPage.addAll(Arrays.asList(
            TextComponent.fromLegacyText("\n" +
                PageCenter.centerLine(
                    StringFormatter.formatHex(textColor + "§lAvailable Guides"),
                true
                )
            )
        ));

        currentPage.addAll(Arrays.asList(
            TextComponent.fromLegacyText("\n" +
                PageCenter.centerLine(
                    StringFormatter.formatHex(textColor + "• • •"),
                    false
                ) + "\n\n"
            )
        ));

        int lineCount = 2;

        for(Guide guide : guides){

            //New page is full

            if(lineCount >= MAX_LINES_PER_PAGE){
                pages.add(currentPage.toArray(new BaseComponent[0]));
                currentPage.clear();

                currentPage.addAll(java.util.Arrays.asList(TextComponent.fromLegacyText(
                    PageCenter.centerLine("\n"+StringFormatter.formatHex(textColor + "§lAvailable Guides§r" + "\n\n" + PageCenter.centerLine("• • •", false) ), false) + "\n\n"
                )));
                lineCount = 2;
            }
            
            BaseComponent[] entry = TextComponent.fromLegacyText(
                StringFormatter.formatHex(textColor + " • " + guide.getId() + "\n")
            );
            for (BaseComponent c : entry) {
                c.setClickEvent(new ClickEvent(
                    ClickEvent.Action.RUN_COMMAND,
                    "/guide " + guide.getId()
                ));
                currentPage.add(c);
            }

            lineCount++;
        }

        

        if(!currentPage.isEmpty()){
            pages.add(currentPage.toArray(new BaseComponent[0]));
        }

        meta.spigot().setPages(pages);

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        book.setItemMeta(meta);

        p.openBook(book);



    }
}
