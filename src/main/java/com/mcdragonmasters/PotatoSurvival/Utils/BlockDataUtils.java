package com.mcdragonmasters.PotatoSurvival.Utils;

import com.mcdragonmasters.PotatoSurvival.PotatoSurvival;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;

import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class BlockDataUtils {

    public static String @Nullable [] getBlockDataTagValues(BlockData blockData) {
        String[] splits1 = blockData.getAsString().split("\\[");
        if (splits1.length >= 2) {
            String[] splits2 = splits1[1].split("]");

            return splits2[0].split(",");
        }
        return null;
    }
    public static @Nullable Object getBlockDataValueFromTag(BlockData blockData, String tag) {
        String[] sp = getBlockDataTagValues(blockData);
        if (sp != null) {
            for (String string : sp) {
                String[] s = string.split("=");
                if (s[0].equals(tag)) {
                    String value = s[1];
                    if (value == null) return null;

                    if (isBoolean(value)) {
                        return Boolean.valueOf(value);
                    } else if (isNumber(value)) {
                        return Integer.parseInt(value);
                    } else {
                        return value;
                    }
                }
            }
        }
        return null;
    }
    public static BlockData setBlockDataTag(BlockData oldBlockData, String tag, Object value) {
        if (oldBlockData.getAsString().contains("[")) {
            String newData = oldBlockData.getMaterial().getKey() + "[" + tag.toLowerCase(Locale.ROOT) + "=" + value + "]";
            try {
                BlockData blockData = Bukkit.createBlockData(newData);
                return oldBlockData.merge(blockData);
            } catch (IllegalArgumentException ex) {
                PotatoSurvival.Logger.warn("Could not parse block data: %s");
            }
        }
        return oldBlockData;
    }
    public static boolean isBoolean(String string) {
        return string.equalsIgnoreCase("true") || string.equalsIgnoreCase("false");
    }
    public static boolean getBooleanBlockDataTag(BlockData blockData, String tag) {
        return Boolean.parseBoolean(BlockDataUtils.getBlockDataValueFromTag(blockData, tag).toString());
    }
    public static boolean isNumber(String string) {
        return string.matches("\\d+");
    }
}
