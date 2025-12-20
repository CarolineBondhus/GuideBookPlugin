package net.tfminecraft.guides.manager;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import net.tfminecraft.guides.guide.Guide;
import net.tfminecraft.guides.loader.GuideLoader;

public class GuideManager {
    public void openBook(Player p) {
    
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setTitle("Halo");
        meta.setAuthor("Caroline");
        Guide guide = GuideLoader.getByString("test");
        for(String page : guide.getPages()){
            meta.addPage(page);
        }
        book.setItemMeta(meta);

        p.openBook(book);
    }
}
