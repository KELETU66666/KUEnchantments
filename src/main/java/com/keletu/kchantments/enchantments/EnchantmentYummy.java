package com.keletu.kchantments.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentYummy extends Enchantment {
    public EnchantmentYummy() {
        super(Rarity.COMMON, KEnchantmentList.BAGUETTE, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        this.setRegistryName("yummy");
        this.setName("yummy");
    }

    @Override
    public boolean canApplyTogether(Enchantment ench) {
        return ench != this && ench != KEnchantmentList.harden;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel)
    {
        return 1 + (enchantmentLevel - 1) * 11;
    }

    @Override
    public int getMaxEnchantability(int ench) {
        return this.getMinEnchantability(ench) + 20;
    }

    public boolean isTreasureEnchantment() {
        return false;
    }

    public boolean isCurse() {
        return false;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

}