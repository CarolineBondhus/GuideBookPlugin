package net.tfminecraft.guides.manager;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.chat.BaseComponent;
import net.tfminecraft.guides.Guides;
import net.tfminecraft.guides.guide.Guide;
import net.tfminecraft.guides.loader.GuideLoader;

public class GuideManager implements Listener{

    public void openBook(Player p, Guide guide, int pageNumber) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();

        // Adds parsed interactive pages
        for(BaseComponent[] page : guide.getParsedPages()){
            meta.spigot().addPage(page);
        }

        int maxPages = meta.getPageCount();

        if (pageNumber < 1) pageNumber = 1;
        if (pageNumber > maxPages) pageNumber = maxPages;

        book.setItemMeta(meta);

        p.openBook(book);
        //TODO support page number opening once mojang allows it (sadge)
    }

    public void editBook(Player p, Guide guide){
        ItemStack book = new ItemStack(Material.WRITABLE_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();

        NamespacedKey key = new NamespacedKey(Guides.getInstance(), "guide_id");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, guide.getId());


        meta.setDisplayName("§f"+guide.getId());
        for(String page : guide.getPages()){
            meta.addPage(page);
        }

        book.setItemMeta(meta);
        p.getInventory().addItem(book);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void writeToBook(PlayerEditBookEvent e) {
        final BookMeta newMeta = e.getNewBookMeta();
        Player p = e.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack item = p.getInventory().getItemInMainHand();
                item.setItemMeta(newMeta);
            }
        }.runTaskLater(Guides.getInstance(), 1L);
    }

    public void saveEditBook(Player p, ItemStack item){
        BookMeta meta = (BookMeta) item.getItemMeta();

        NamespacedKey key = new NamespacedKey(Guides.getInstance(), "guide_id");
        String guideId = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        if(guideId == null){
            p.sendMessage("§cThis book is not an editable guide");
            return;
        }

        Guide guide = GuideLoader.getByString(guideId);
        guide.setPages(meta.getPages());

        GuideLoader.save(guide, p);

        p.getInventory().getItemInMainHand().setAmount(0);
        p.sendMessage("§aSaved " + guideId);
    }
}
