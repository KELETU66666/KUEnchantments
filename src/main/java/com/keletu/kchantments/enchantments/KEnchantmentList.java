package com.keletu.kchantments.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemTool;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class KEnchantmentList {
    public static final EnumEnchantmentType
            IRON_GEAR = EnumHelper.addEnchantmentType("IRON_GEAR", (item) -> (item instanceof ItemTool && ((ItemTool) item).getToolMaterialName().equals(Item.ToolMaterial.IRON.name())) || (item instanceof ItemArmor && ((ItemArmor) item).getArmorMaterial().equals(ItemArmor.ArmorMaterial.IRON)));
            //DESTRUCTIVE = EnumHelper.addEnchantmentType("DESTRUCTIVE", (item) -> item instanceof IEnchantDestructive);

    public static final Enchantment swordSplash = new EnchantmentSwordSplash();
    public static final Enchantment extravagance = new EnchantmentExtravagance();
    public static final Enchantment bronzeTeeth = new EnchantmentBronzeTeeth();
    public static final Enchantment restrictedView = new EnchantmentRestrictedView();
    public static void setupEnchatments(){
        ForgeRegistries.ENCHANTMENTS.register(swordSplash);
        ForgeRegistries.ENCHANTMENTS.register(extravagance);
        ForgeRegistries.ENCHANTMENTS.register(bronzeTeeth);
        ForgeRegistries.ENCHANTMENTS.register(restrictedView);
    }
}
