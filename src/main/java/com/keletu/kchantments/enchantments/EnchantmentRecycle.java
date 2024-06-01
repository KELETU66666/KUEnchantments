package com.keletu.kchantments.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentRecycle extends Enchantment {
    public EnchantmentRecycle() {
        super(Rarity.VERY_RARE, KEnchantmentList.BAGUETTE, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        this.setRegistryName("recycle");
        this.setName("recycle");
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
        return true;
    }
    @Override
    public int getMaxLevel() {
        return 1;
    }

}