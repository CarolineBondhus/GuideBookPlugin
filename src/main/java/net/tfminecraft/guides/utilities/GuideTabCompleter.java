package net.tfminecraft.guides.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import net.tfminecraft.guides.loader.GuideLoader;
import net.tfminecraft.guides.guide.Guide;

public class GuideTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args
    ) {

        // /guide <argument>
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();

            completions.add("create");
            completions.add("edit");
            completions.add("save");

            // legg til alle guider
            for (Guide guide : GuideLoader.get()) {
                completions.add(guide.getId());
            }

            return filter(completions, args[0]);
        }

        // /guide edit <guideId>
        if (args.length == 2 && args[0].equalsIgnoreCase("edit")) {
            List<String> guides = GuideLoader.get()
                    .stream()
                    .map(Guide::getId)
                    .collect(Collectors.toList());

            return filter(guides, args[1]);
        }

        // /guide <guideId> <page>
        if (args.length == 2) {
            return Collections.singletonList("<page>");
        }

        return Collections.emptyList();
    }

    private List<String> filter(List<String> list, String input) {
        return list.stream()
                .filter(s -> s.toLowerCase().startsWith(input.toLowerCase()))
                .collect(Collectors.toList());
    }
}
