package com.keletu.kchantments.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.keletu.kchantments.Reference;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemBaguette extends Item {

    public static final String NBTTAG_DURABILITY = "Freshness";
    private static final int MaxDurability = 96000;

    public ItemBaguette()
    {
        this.setRegistryName(Reference.MOD_ID, "baguette");
        this.setTranslationKey(Reference.MOD_ID + ":" + "baguette");
        this.maxStackSize = 1;
        this.setMaxDamage(30);
        this.setCreativeTab(CreativeTabs.COMBAT);
    }


    @Override
    @Nonnull
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        stack.setItemDamage(stack.getItemDamage() + 1);

        if(entityLiving instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) entityLiving;
            entityplayer.getFoodStats().addStats(1, 0.3F);
            worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            StatBase statBase = StatList.getObjectUseStats(this);
            assert statBase != null;
            entityplayer.addStat(statBase);
        }

        return stack;
    }

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        stack.damageItem(1, attacker);
        return true;
    }

    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }

    public int getItemEnchantability()
    {
        return 23;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        if(itemStackIn.getMaxDamage() - itemStackIn.getItemDamage()> 0) {
            playerIn.setActiveHand(hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
        }
        else {
            return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
        }
    }

    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return par2ItemStack.isItemEqual(new ItemStack(Items.BREAD)) || super.getIsRepairable(par1ItemStack, par2ItemStack);
    }

    public static int getNBTDurability(ItemStack pItemStack) {
        if(pItemStack.getTagCompound() == null)
        {
            pItemStack.setTagCompound(new NBTTagCompound());
            pItemStack.getTagCompound().setInteger(NBTTAG_DURABILITY, MaxDurability);
            return pItemStack.getTagCompound().getInteger(NBTTAG_DURABILITY);
        }
        return pItemStack.getTagCompound().getInteger(NBTTAG_DURABILITY);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        createOrInitNBTTag(stack);
        int tCurrentDura = getNBTDurability(stack);
        if(stack.getTagCompound() != null) {
            if (tCurrentDura > 0)
                stack.getTagCompound().setInteger(NBTTAG_DURABILITY, --tCurrentDura);
            else
                stack.shrink(1);
        }
    }

    private void createOrInitNBTTag(ItemStack pItemStack) {
        if (pItemStack.getTagCompound() == null) {
            pItemStack.setTagCompound(new NBTTagCompound());
            pItemStack.getTagCompound().setInteger(NBTTAG_DURABILITY, MaxDurability);
        }
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        HashMultimap<String, AttributeModifier> modifiers = HashMultimap.create();
        if (slot == EntityEquipmentSlot.MAINHAND && stack.getTagCompound() != null) {
            modifiers.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4, 0));
            modifiers.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon Modifier", 8F * stack.getTagCompound().getInteger(NBTTAG_DURABILITY)/MaxDurability, 0));
        }
        return modifiers;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, @Nullable World worldIn, List<String> lines, ITooltipFlag flagIn) {
        if (itemStack.getTagCompound() != null) {
            if (itemStack.getTagCompound().getInteger(NBTTAG_DURABILITY) > 0)
                lines.add("This bread looks like it should be edible...probably");
            else
                lines.add("This 'bread' looks completely useless");
            if (itemStack.getTagCompound().getInteger(NBTTAG_DURABILITY) >= 48000) {
                lines.add(TextFormatting.BOLD + TextFormatting.GREEN.toString() + I18n.format("Fresh"));
            } else if (itemStack.getTagCompound().getInteger(NBTTAG_DURABILITY) >= 19200 && itemStack.getTagCompound().getInteger(NBTTAG_DURABILITY) < 48000) {
                lines.add(TextFormatting.BOLD + TextFormatting.GOLD.toString() + I18n.format("Stale"));
            } else if (itemStack.getTagCompound().getInteger(NBTTAG_DURABILITY) < 19200 && itemStack.getTagCompound().getInteger(NBTTAG_DURABILITY) > 0) {
                lines.add(TextFormatting.BOLD + TextFormatting.RED.toString() + I18n.format("Spoiled"));
            } else
                lines.add(TextFormatting.BOLD + TextFormatting.DARK_RED.toString() + I18n.format("Rot"));
        }
    }
}
