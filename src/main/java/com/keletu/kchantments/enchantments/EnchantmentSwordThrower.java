package com.keletu.kchantments.enchantments;


import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentSwordThrower extends Enchantment {
    public EnchantmentSwordThrower() {
        super(Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        this.setRegistryName("sword_thrower");
        this.setName("sword_thrower");
    }

    @Override
    public boolean canApplyTogether(Enchantment ench) {
        return ench != Enchantments.FIRE_ASPECT && ench != Enchantments.KNOCKBACK && ench != this;
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
        return true;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

}