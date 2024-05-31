package com.keletu.kchantments.event;

import com.keletu.kchantments.ConfigKE;
import com.keletu.kchantments.Kchantments;
import com.keletu.kchantments.Reference;
import com.keletu.kchantments.enchantments.KEnchantmentList;
import com.keletu.kchantments.packet.PacketSummonGhostSword;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class Kevents {

    @SubscribeEvent
    public static void SwordUseEvent(PlayerInteractEvent.LeftClickEmpty event) {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = player.getHeldItemMainhand();
        if (stack.getItem() instanceof ItemSword && EnchantmentHelper.getEnchantmentLevel(KEnchantmentList.swordSplash, stack) > 0) {
            Kchantments.packetInstance.sendToServer(new PacketSummonGhostSword());
        }
    }

    @SubscribeEvent
    public static void RemoveDrops(BlockEvent.HarvestDropsEvent event){
        if(event.getWorld().isRemote)
            return;

        EntityPlayer player = event.getHarvester();
        if (player != null) {
            player.inventory.getCurrentItem();
            ItemStack equip = player.inventory.getCurrentItem();
            if (EnchantmentHelper.getEnchantmentLevel(KEnchantmentList.extravagance, equip) > 0) {
                for (int x = 0; x < event.getDrops().size(); x++) {
                    ItemStack drop = event.getDrops().get(x);
                    if (drop != null) {
                        event.getDrops().remove(x);
                        player.world.spawnEntity(new EntityXPOrb(player.world, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), player.world.rand.nextInt(3) + 1));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void RestrictedViews(RenderGameOverlayEvent event){
        ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());

        if(Minecraft.getMinecraft().player == null)
            return;

        if(event.getType() != RenderGameOverlayEvent.ElementType.HELMET)
            return;

        ItemStack itemstack = Minecraft.getMinecraft().player.inventory.armorItemInSlot(3);

        if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 && EnchantmentHelper.getEnchantmentLevel(KEnchantmentList.restrictedView, itemstack) > 0 && !ConfigKE.restrictBlacklist.contains(itemstack.getItem().getRegistryName()))
        {
            renderRestrictedViewOverlay(scaledresolution);
        }
    }

    protected static void renderRestrictedViewOverlay(ScaledResolution scaledRes)
    {
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableAlpha();
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(Reference.MOD_ID, "textures/gui/helmblur.png"));
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0.0D, scaledRes.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
        bufferbuilder.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
        bufferbuilder.pos(scaledRes.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @SubscribeEvent
    public static void EatYourTools(PlayerInteractEvent.RightClickItem event){
        if(event.getWorld().isRemote)
            return;

        EntityPlayer player = event.getEntityPlayer();
        World world = event.getWorld();
        for(EnumHand hand : EnumHand.values()) {
            ItemStack stack = player.getHeldItem(hand);
            if (EnchantmentHelper.getEnchantmentLevel(KEnchantmentList.bronzeTeeth, stack) > 0) {
                event.setCanceled(true);
                world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
                player.getFoodStats().addStats(EnchantmentHelper.getEnchantmentLevel(KEnchantmentList.bronzeTeeth, stack) * 2, (float) (EnchantmentHelper.getEnchantmentLevel(KEnchantmentList.bronzeTeeth, stack) * 1.25));
                if (player instanceof EntityPlayerMP) {
                    CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP) player, stack);
                }

                stack.shrink(1);
            }
        }
    }
}