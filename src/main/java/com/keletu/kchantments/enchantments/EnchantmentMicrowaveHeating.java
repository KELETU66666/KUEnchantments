package com.keletu.kchantments.enchantments;

import com.keletu.kchantments.item.ItemBaguette;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentMicrowaveHeating extends Enchantment {
    public EnchantmentMicrowaveHeating() {
        super(Rarity.UNCOMMON, KEnchantmentList.BAGUETTE, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        this.setRegistryName("microwave_heating");
        this.setName("microwave_heating");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean canApplyTogether(Enchantment ench) {
        return ench != this;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel)
    {
        return 10 + 20 * (enchantmentLevel - 1);
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }

    public boolean isTreasureEnchantment() {
        return false;
    }

    public boolean isCurse() {
        return false;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @SubscribeEvent
    public void IgniteTarget(LivingAttackEvent event) {
        if(!(event.getSource().getTrueSource() instanceof EntityLivingBase) || event.getEntity().world.isRemote)
            return;

        EntityLivingBase living = (EntityLivingBase) event.getSource().getTrueSource();
        ItemStack stack = living.getHeldItemMainhand();
        if (stack.getItem() instanceof ItemBaguette && EnchantmentHelper.getEnchantmentLevel(KEnchantmentList.microwaveHeating, stack) > 0) {
            event.getEntityLiving().setFire(EnchantmentHelper.getEnchantmentLevel(KEnchantmentList.microwaveHeating, stack) * 3);
        }
    }

}