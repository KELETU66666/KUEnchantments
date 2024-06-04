package com.keletu.kchantments.enchantments;

import com.keletu.kchantments.Kchantments;
import com.keletu.kchantments.packet.PacketRageParticles;
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
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class EnchantmentFurious extends Enchantment {
    public EnchantmentFurious() {
        super(Rarity.COMMON, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        this.setRegistryName("furious");
        this.setName("furious");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean canApplyTogether(Enchantment ench) {
        return ench != this && ench != KEnchantmentList.elbowing && ench != KEnchantmentList.sedation && ench != Enchantments.SHARPNESS;
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
    public void BoostCriticalAttack(LivingHurtEvent event) {
        if (event.getEntity() == null || event.getSource().getTrueSource() == null || !(event.getSource().getTrueSource() instanceof EntityPlayer))
            return;

        EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
        World world = player.world;
        int enchLev = EnchantmentHelper.getEnchantmentLevel(this, player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND));
        if (enchLev > 0)
            if (!player.isSprinting() && !player.onGround) {
                event.setAmount(event.getAmount() + enchLev * 0.75F);
                for (int i = 0; i < 5; ++i) {
                    double d0 = world.rand.nextGaussian() * 0.02D;
                    double d1 = world.rand.nextGaussian() * 0.02D;
                    double d2 = world.rand.nextGaussian() * 0.02D;
                    Kchantments.packetInstance.sendToAllAround(new PacketRageParticles(player.posX + (double) (world.rand.nextFloat() * player.width * 2.0F) - (double) player.width, player.posY + 1.0D + (double) (world.rand.nextFloat() * player.height), player.posZ + (double) (world.rand.nextFloat() * player.width * 2.0F) - (double) player.width, d0, d1, d2), new NetworkRegistry.TargetPoint(player.world.provider.getDimension(), player.posX, player.posY, player.posZ, 64));
                }
            } else {
                event.setAmount(Math.min(0, event.getAmount() - enchLev * 0.375F));
            }
    }
}
