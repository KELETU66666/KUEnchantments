package com.keletu.kchantments;

import com.keletu.kchantments.entities.EntitySplash;
import com.keletu.kchantments.item.ItemBaguette;
import com.keletu.kchantments.packet.PacketSummonGhostSword;
import com.keletu.kchantments.proxy.CommonProxy;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class Kchantments {

    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_NAME);

    public static final String serverProxy = "com.keletu.kchantments.proxy.CommonProxy";
    public static final String clientProxy = "com.keletu.kchantments.proxy.ClientProxy";

    public static Item baguette = new ItemBaguette();

    @SidedProxy(serverSide = serverProxy, clientSide = clientProxy)
    public static CommonProxy proxy;

    public static SimpleNetworkWrapper packetInstance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigKE.onConfig(event);

        packetInstance = NetworkRegistry.INSTANCE.newSimpleChannel("KEChannel");
        packetInstance.registerMessage(PacketSummonGhostSword.Handler.class, PacketSummonGhostSword.class, 0, Side.SERVER);

        EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID + ":" + "splash"), EntitySplash.class, "sword_splash", 0, this, 32, 1, true);
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventBusSubscriber
    public static class ObjectRegistryHandler {

        @SubscribeEvent
        public static void addItems(RegistryEvent.Register<Item> event) {
            event.getRegistry().register(baguette);
        }

        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void modelRegistryEvent(ModelRegistryEvent event) {
            ModelLoader.setCustomModelResourceLocation(baguette, 0, new ModelResourceLocation(baguette.getRegistryName(), "inventory"));
        }
    }
}
