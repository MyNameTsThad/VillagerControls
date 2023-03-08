package com.thaddev.villagercontrols.core;

import com.thaddev.villagercontrols.VillagerControls;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.Arrays;

public class Utils {
    public static String black = "§0";
    public static String dark_blue = "§1";
    public static String dark_green = "§2";
    public static String dark_aqua = "§3";
    public static String dark_red = "§4";
    public static String dark_purple = "§5";
    public static String gold = "§6";
    public static String gray = "§7";
    public static String dark_gray = "§8";
    public static String blue = "§9";
    public static String green = "§a";
    public static String aqua = "§b";
    public static String red = "§c";
    public static String light_purple = "§d";
    public static String yellow = "§e";
    public static String white = "§f";
    public static String obfuscated = "§k";
    public static String bold = "§l";
    public static String strikethrough = "§m";
    public static String underline = "§n";
    public static String italic = "§o";
    public static String reset = "§r";

    public static String getColorFromCode(String color) {
        try {
            Field colorField = Utils.class.getField(color.substring(2));
            return colorField.get("").toString();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            VillagerControls.LOGGER.error(Arrays.toString(e.getStackTrace()));
            return "";
        }
    }

    public static String from(String fromText) {
        return convert(fromText, dark_aqua + "[" + gold + "VillagerControls" + dark_aqua + "] " + reset);
    }

    public static String fromNoTag(String fromText) {
        return convert(fromText, "");
    }

    public static MutableComponent component(String fromText) {
        return (MutableComponent) Component.nullToEmpty(fromText);
    }

    public static String convert(String fromText, String initial) {
        char[] chars = fromText.toCharArray();
        StringBuilder builder = new StringBuilder(initial);
        int startIndex = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '(' && chars[i + 1] == '%' && chars[i + 2] == '$') {
                startIndex = i + 1;
            } else if (chars[i] == ')' && startIndex != 0) {
                builder.append(getColorFromCode(fromText.substring(startIndex, i)));
                startIndex = 0;
            } else if (startIndex == 0) {
                builder.append(chars[i]);
            }
        }
        return builder.toString();
    }

    public static int rgbToInteger(int red, int green, int blue, int alpha) {
        return (alpha & 0xFF) << 24 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF);
    }

    public static int rgbToInteger(int red, int green, int blue) {
        return rgbToInteger(red, green, blue, 255);
    }

    public static Color integerToColor(int color) {
        return new Color(color);
    }

    public static String niceify(String text){
        text = text.toLowerCase();
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
