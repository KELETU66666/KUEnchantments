package com.keletu.kchantments.enchantments;


import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentSwordSplash extends Enchantment {
    public EnchantmentSwordSplash() {
        super(Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        this.setRegistryName("sword_splash");
        this.setName("sword_splash");
    }

    @Override
    public boolean canApplyTogether(Enchantment ench) {
        return ench != Enchantments.FIRE_ASPECT && ench != Enchantments.KNOCKBACK && ench != this;
    }

    @Override
    public int getMinEnchantability(int ench) {
        return 20 + 15 * (ench - 1);
    }

    @Override
    public int getMaxEnchantability(int ench) {
        return this.getMinEnchantability(ench) + 40;
    }

    public boolean isTreasureEnchantment() {
        return false;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

}