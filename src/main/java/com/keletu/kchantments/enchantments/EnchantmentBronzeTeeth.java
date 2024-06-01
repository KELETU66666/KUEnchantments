package com.keletu.kchantments.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentBronzeTeeth extends Enchantment {
    public EnchantmentBronzeTeeth() {
        super(Rarity.VERY_RARE, KEnchantmentList.IRON_GEAR, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        this.setRegistryName("bronze_teeth");
        this.setName("bronze_teeth");
    }

    @Override
    public boolean canApplyTogether(Enchantment ench) {
        return ench != this;
    }

    @Override
    public int getMinEnchantability(int ench) {
        return 30 + 5 * (ench - 1);
    }

    @Override
    public int getMaxEnchantability(int ench) {
        return this.getMinEnchantability(ench) + 40;
    }

    public boolean isTreasureEnchantment() {
        return false;
    }

    public boolean isCurse() {
        return true;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

}