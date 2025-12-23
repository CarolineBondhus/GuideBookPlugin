package net.tfminecraft.guides.manager;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import net.tfminecraft.guides.guide.Guide;

public class GuideManager {

    public void openBook(Player p, Guide guide) {
    
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        // Adds each guide page as a page in the book
        for(String page : guide.getPages()){
            meta.addPage(page);
        }
        book.setItemMeta(meta);
        

        p.openBook(book);
    }
}
