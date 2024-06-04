package com.keletu.kchantments.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentRestrictedView extends Enchantment {
    public EnchantmentRestrictedView() {
        super(Rarity.VERY_RARE, EnumEnchantmentType.ARMOR_HEAD, new EntityEquipmentSlot[]{EntityEquipmentSlot.HEAD});
        this.setRegistryName("restricted_view");
        this.setName("restricted_view");
    }

    @Override
    public boolean canApplyTogether(Enchantment ench) {
        return ench != this;
    }

    @Override
    public int getMinEnchantability(int ench) {
        return 40;
    }

    @Override
    public int getMaxEnchantability(int ench) {
        return this.getMinEnchantability(ench);
    }

    public boolean isTreasureEnchantment() {
        return false;
    }

    public boolean isCurse() {
        return true;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

}