package com.keletu.kchantments.enchantments;

import com.keletu.kchantments.util.BlockCoord;
import com.keletu.kchantments.util.KUEUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EnchantmentNightTerrors extends Enchantment {
    public EnchantmentNightTerrors() {
        super(Rarity.VERY_RARE, EnumEnchantmentType.ARMOR_HEAD, new EntityEquipmentSlot[]{EntityEquipmentSlot.HEAD});
        this.setRegistryName("night_terrors");
        this.setName("night_terrors");
        MinecraftForge.EVENT_BUS.register(this);
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
        return 3;
    }

    @SubscribeEvent
    public void FearTheDarkness(TickEvent.PlayerTickEvent event){
        if(event.player.world.isRemote)
            return;

        EntityPlayer player = event.player;
        World world = player.world;
        int enchLev = EnchantmentHelper.getEnchantmentLevel(this, player.getItemStackFromSlot(EntityEquipmentSlot.HEAD));
        if(world.getLight(player.getPosition()) < 5 && enchLev > 0 && player.world.rand.nextInt(30) < enchLev) {
            BlockCoord target = KUEUtils.randomBlock(world, player, enchLev * 3, block -> KUEUtils.hasNonSolidNeighbor(world, block));
            if (target == null) {
                return;
            }

            BlockCoord below = target.copy().offset(0);
            if (target.isAir(world) && !below.isAir(world)) {
                world.setBlockState(new BlockPos(target.x, target.y, target.z), Blocks.FIRE.getDefaultState(), 11);
                world.playSound(target.x, target.y, target.z, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 1.0F, false);
            }
        }
    }
}