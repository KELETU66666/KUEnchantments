package com.keletu.kchantments.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentSedation extends Enchantment {
    public EnchantmentSedation() {
        super(Rarity.COMMON, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        this.setRegistryName("sedation");
        this.setName("sedation");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean canApplyTogether(Enchantment ench) {
        return ench != this && ench != KEnchantmentList.furious && ench != KEnchantmentList.elbowing && ench != Enchantments.SHARPNESS;
    }
    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 2 + (enchantmentLevel - 1) * 12;
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

    @SubscribeEvent
    public void BoostNormalAttack(LivingHurtEvent event) {
        if (event.getEntity() == null || event.getSource().getTrueSource() == null || !(event.getSource().getTrueSource() instanceof EntityPlayer))
            return;

        EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
        World world = player.world;
        int enchLev = EnchantmentHelper.getEnchantmentLevel(this, player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND));
        if (enchLev > 0)
            if (!player.isSprinting() && player.onGround) {
                event.setAmount(event.getAmount() + enchLev * 1.25F);
            } else {
                event.setAmount(Math.min(0, event.getAmount() - enchLev * 0.5F));
            }
    }
}
