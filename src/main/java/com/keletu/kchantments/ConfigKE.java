package com.keletu.kchantments;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigKE {

    public static int BaguetteFoodLevel;
    public static float BaguetteSaturationModifier;
    public static final List<ResourceLocation> restrictBlacklist = new ArrayList<>();

    public static void onConfig(FMLPreInitializationEvent builder) {

        Configuration config = new Configuration(builder.getSuggestedConfigurationFile());

        BaguetteFoodLevel = config.getInt("BaguetteFoodLevel", "Generic Config", 4, 1, 20, "How many food point baguette restores");

        BaguetteSaturationModifier = config.getFloat("BaguetteSaturationModifier", "Generic Config", 0.3F, 0, 20, "How many saturation point baguette restores");

        restrictBlacklist.clear();
        String[] cursed = config.getStringList("RestrictBlacklist", "Restricted View", new String[]{"minecraft:leather_helmet", "minecraft:chainmail_helmet"}, "List of item ignore Restricted View Enchant"
                + "Examples: minecraft:leather_helmet, minecraft:chainmail_helmet. Changing this option required game restart to take effect.");

        Arrays.stream(cursed).forEach(entry -> restrictBlacklist.add(new ResourceLocation(entry)));


        config.save();
    }
}
