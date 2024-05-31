package com.keletu.kchantments.proxy;

import com.keletu.kchantments.client.RenderSplash;
import com.keletu.kchantments.entities.EntitySplash;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy{
    public void init(FMLInitializationEvent e) {
        super.init(e);
        RenderingRegistry.registerEntityRenderingHandler(EntitySplash.class, new RenderSplash<>(Minecraft.getMinecraft().getRenderManager(), Minecraft.getMinecraft().getRenderItem()));
    }
}
