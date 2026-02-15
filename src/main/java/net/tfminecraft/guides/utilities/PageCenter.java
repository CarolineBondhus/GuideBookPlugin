package net.tfminecraft.guides.utilities;

import net.tfminecraft.guides.enums.CharWidth;

public class PageCenter {
    public static String centerLine(String line, boolean bold) {

        //Width in pixels
        final int LINE_WIDTH = 114;

        // Remove formatting codes for length calculation
        String stripped = line
            .replaceAll("(?i)§x(§[0-9a-f]){6}", "") // remove hex colors
            .replaceAll("§[0-9a-fk-or]", "")        // remove normal codes
            .trim();
        
        //Divides text into letters
        char[] letters = stripped.toCharArray();

        int length = 0;

        for(char c : letters){
            length += CharWidth.getWidth(c, bold);
        }

        int space = Math.max(0, (LINE_WIDTH - length) / 2);
        
        int spacePixels = Math.max(0, space / 4);

        return " ".repeat(spacePixels) + line;
    }

}
