package com.keletu.kchantments.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentExtravagance extends Enchantment {
    public EnchantmentExtravagance() {
        super(Rarity.VERY_RARE, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        this.setRegistryName("extravagance");
        this.setName("extravagance");
    }

    @Override
    public boolean canApplyTogether(Enchantment ench) {
        return ench != this;
    }

    @Override
    public int getMinEnchantability(int ench) {
        return 35;
    }

    @Override
    public int getMaxEnchantability(int ench) {
        return this.getMinEnchantability(ench);
    }

    public boolean isTreasureEnchantment() {
        return false;
    }

    public boolean isCurse() {
        return false;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

}