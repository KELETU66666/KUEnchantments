package com.keletu.kchantments.enchantments;

import com.keletu.kchantments.item.ItemBaguette;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemTool;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class KEnchantmentList {
    public static final EnumEnchantmentType
            IRON_GEAR = EnumHelper.addEnchantmentType("IRON_GEAR", (item) -> (item instanceof ItemTool && ((ItemTool) item).getToolMaterialName().equals(Item.ToolMaterial.IRON.name())) || (item instanceof ItemArmor && ((ItemArmor) item).getArmorMaterial().equals(ItemArmor.ArmorMaterial.IRON))),
            BAGUETTE = EnumHelper.addEnchantmentType("BAGUETTE", (item) -> item instanceof ItemBaguette);

    public static final Enchantment swordSplash = new EnchantmentSwordSplash();
    public static final Enchantment extravagance = new EnchantmentExtravagance();
    public static final Enchantment bronzeTeeth = new EnchantmentBronzeTeeth();
    public static final Enchantment restrictedView = new EnchantmentRestrictedView();
    public static final Enchantment yummy = new EnchantmentYummy();
    public static final Enchantment microwaveHeating = new EnchantmentMicrowaveHeating();
    public static final Enchantment harden = new EnchantmentHarden();
    public static final Enchantment recycle = new EnchantmentRecycle();
    public static final Enchantment nightTerrors = new EnchantmentNightTerrors();
    public static final Enchantment elbowing = new EnchantmentElbowing();
    public static final Enchantment furious = new EnchantmentFurious();
    public static final Enchantment sedation = new EnchantmentSedation();
    public static void setupEnchatments(){
        ForgeRegistries.ENCHANTMENTS.register(swordSplash);
        ForgeRegistries.ENCHANTMENTS.register(extravagance);
        ForgeRegistries.ENCHANTMENTS.register(bronzeTeeth);
        ForgeRegistries.ENCHANTMENTS.register(restrictedView);
        ForgeRegistries.ENCHANTMENTS.register(yummy);
        ForgeRegistries.ENCHANTMENTS.register(microwaveHeating);
        ForgeRegistries.ENCHANTMENTS.register(harden);
        ForgeRegistries.ENCHANTMENTS.register(recycle);
        ForgeRegistries.ENCHANTMENTS.register(nightTerrors);
        ForgeRegistries.ENCHANTMENTS.register(elbowing);
        ForgeRegistries.ENCHANTMENTS.register(furious);
        ForgeRegistries.ENCHANTMENTS.register(sedation);
    }
}
