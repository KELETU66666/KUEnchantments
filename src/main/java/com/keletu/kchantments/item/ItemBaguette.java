package com.keletu.kchantments.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import static com.keletu.kchantments.ConfigKE.BaguetteFoodLevel;
import static com.keletu.kchantments.ConfigKE.BaguetteSaturationModifier;
import com.keletu.kchantments.Reference;
import com.keletu.kchantments.enchantments.KEnchantmentList;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemBaguette extends Item {

    public static final String NBTTAG_DURABILITY = "Freshness";
    public static final String NBTTAG_LEFT = "Bread";
    public static final int MaxDurability = 4800;
    public static final int MaxUse = 10;

    public ItemBaguette() {
        this.setRegistryName(Reference.MOD_ID, "baguette");
        this.setTranslationKey(Reference.MOD_ID + ":" + "baguette");
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.COMBAT);
        this.addPropertyOverride(new ResourceLocation("recycle"), new IItemPropertyGetter() {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                if (EnchantmentHelper.getEnchantmentLevel(KEnchantmentList.recycle, stack) > 0 && stack.getTagCompound() != null && getNBTUse(stack) <= 0) {
                    return 2.0F;
                } else if (EnchantmentHelper.getEnchantmentLevel(KEnchantmentList.recycle, stack) > 0) {
                    return 1.0F;
                }

                return 0.0F;
            }
        });
    }

    public int getMaxItemUseDuration(ItemStack stack) {
        return 16;
    }

    @Override
    @Nonnull
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        createOrInitNBTTag(stack);
        int tCurrentUse = getNBTUse(stack);
        if (stack.getTagCompound() != null) {
            if (tCurrentUse > 0)
                stack.getTagCompound().setInteger(NBTTAG_LEFT, --tCurrentUse);
        }

        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) entityLiving;
            entityplayer.getFoodStats().addStats(BaguetteFoodLevel + yummyLevel(stack), BaguetteSaturationModifier + yummyLevel(stack) * 0.3F);
            entityplayer.attackEntityFrom(DamageSource.STARVE, 2 - heatingLevel(stack) + hardenLevel(stack) * 2);
            if (heatingLevel(stack) > 2)
                entityplayer.setFire(5 * (heatingLevel(stack) - 2));

            worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            StatBase statBase = StatList.getObjectUseStats(this);
            assert statBase != null;
            entityplayer.addStat(statBase);
        }

        return stack;
    }

    public int yummyLevel(ItemStack stack) {
        return EnchantmentHelper.getEnchantmentLevel(KEnchantmentList.yummy, stack);
    }

    public int heatingLevel(ItemStack stack) {
        return EnchantmentHelper.getEnchantmentLevel(KEnchantmentList.microwaveHeating, stack);
    }

    public int hardenLevel(ItemStack stack) {
        return EnchantmentHelper.getEnchantmentLevel(KEnchantmentList.harden, stack);
    }

    public void consumeOrNot(ItemStack stack) {
        if (!(EnchantmentHelper.getEnchantmentLevel(KEnchantmentList.recycle, stack) > 0))
            stack.shrink(1);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.EAT;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        if (itemStackIn.getTagCompound() != null && getNBTUse(itemStackIn) > 0) {
            playerIn.setActiveHand(hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
        } else {
            return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }

    public int getItemEnchantability() {
        return 23;
    }

    public static int getNBTDurability(ItemStack stack) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setInteger(NBTTAG_DURABILITY, MaxDurability);
            return stack.getTagCompound().getInteger(NBTTAG_DURABILITY);
        }
        if (getNBTUse(stack) <= 0) {
            stack.getTagCompound().setInteger(NBTTAG_DURABILITY, 0);
        }
        return stack.getTagCompound().getInteger(NBTTAG_DURABILITY);
    }

    public static int getNBTUse(ItemStack stack) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setInteger(NBTTAG_LEFT, MaxUse);
            return stack.getTagCompound().getInteger(NBTTAG_LEFT);
        }
        return stack.getTagCompound().getInteger(NBTTAG_LEFT);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        createOrInitNBTTag(stack);
        int tCurrentDura = getNBTDurability(stack);
        if (stack.getTagCompound() != null && worldIn.getTotalWorldTime() % 100 == 0) {
            if (tCurrentDura > 0)
                stack.getTagCompound().setInteger(NBTTAG_DURABILITY, --tCurrentDura);
            else
                consumeOrNot(stack);
        }
        if (getNBTUse(stack) <= 0) {
            consumeOrNot(stack);
        }
    }

    private void createOrInitNBTTag(ItemStack stack) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
        if (!stack.getTagCompound().hasKey(NBTTAG_DURABILITY)) {
            stack.getTagCompound().setInteger(NBTTAG_DURABILITY, MaxDurability);
        }
        if (!stack.getTagCompound().hasKey(NBTTAG_LEFT)) {
            stack.getTagCompound().setInteger(NBTTAG_LEFT, MaxUse);
        }
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        HashMultimap<String, AttributeModifier> modifiers = HashMultimap.create();
        if (slot == EntityEquipmentSlot.MAINHAND && stack.getTagCompound() != null) {
            modifiers.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4, 0));
            modifiers.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon Modifier", 6F * stack.getTagCompound().getInteger(NBTTAG_DURABILITY) / MaxDurability + EnchantmentHelper.getEnchantmentLevel(KEnchantmentList.harden, stack) * 1.25, 0));
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
            lines.add("");
            if (itemStack.getTagCompound().getInteger(NBTTAG_DURABILITY) >= 48000 / 20) {
                lines.add(TextFormatting.GREEN + TextFormatting.BOLD.toString() + I18n.format("Fresh"));
            } else if (itemStack.getTagCompound().getInteger(NBTTAG_DURABILITY) >= 19200 / 20 && itemStack.getTagCompound().getInteger(NBTTAG_DURABILITY) < 48000) {
                lines.add(TextFormatting.GOLD + TextFormatting.BOLD.toString() + I18n.format("Stale"));
            } else if (itemStack.getTagCompound().getInteger(NBTTAG_DURABILITY) < 19200 / 20 && itemStack.getTagCompound().getInteger(NBTTAG_DURABILITY) > 0) {
                lines.add(TextFormatting.RED + TextFormatting.BOLD.toString() + I18n.format("Spoiled"));
            } else
                lines.add(TextFormatting.BOLD + TextFormatting.DARK_RED.toString() + I18n.format("Rot"));
            lines.add(TextFormatting.DARK_PURPLE + TextFormatting.BOLD.toString() + itemStack.getTagCompound().getInteger(NBTTAG_LEFT) + TextFormatting.RESET + " remaining");
        }
    }
}
