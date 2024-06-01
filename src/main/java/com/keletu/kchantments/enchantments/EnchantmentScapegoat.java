package com.keletu.kchantments.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class EnchantmentScapegoat extends Enchantment {
    public EnchantmentScapegoat() {
        super(Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        this.setRegistryName("scapegoat");
        this.setName("scapegoat");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean canApplyTogether(Enchantment ench) {
        return ench != Enchantments.SWEEPING && ench != this;
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
    public boolean isCurse() {
        return true;
    }
    @Override
    public int getMaxLevel() {
        return 4;
    }

    @SubscribeEvent
    public void ChooseScapegoat(LivingAttackEvent event){
        if(event.getEntityLiving().world.isRemote || !(event.getSource().getTrueSource() instanceof EntityPlayer) || event.isCanceled())
            return;

        EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
        if(event.getAmount() > 0 && EnchantmentHelper.getEnchantmentLevel(this, player.getHeldItemMainhand()) > 0){
            List<Entity> mobs = event.getEntity().world.getEntitiesWithinAABBExcludingEntity(event.getEntity(), new AxisAlignedBB(event.getEntity().posX-6, event.getEntity().posY-6, event.getEntity().posZ-6, event.getEntity().posX+6, event.getEntity().posY+6, event.getEntity().posZ+6));

            if(!mobs.isEmpty() & mobs.size() > 0)
            {
                Entity randomEntity = mobs.get((int) (Math.random() * (mobs.size() - 1)));
                randomEntity.attackEntityFrom(event.getSource(), event.getAmount() * 0.25F * EnchantmentHelper.getEnchantmentLevel(this, player.getHeldItemMainhand()));
            }
        }
    }

}