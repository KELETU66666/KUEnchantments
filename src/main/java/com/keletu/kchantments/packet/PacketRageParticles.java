package com.keletu.kchantments.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRageParticles implements IMessage {

    private double x;
    private double y;
    private double z;
    private double xs;
    private double ys;
    private double zs;
    private boolean check;

    public PacketRageParticles() {
    }

    public PacketRageParticles(double x, double y, double z, double xs, double ys, double zs) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.xs = xs;
        this.ys = ys;
        this.zs = zs;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeDouble(this.xs);
        buf.writeDouble(this.ys);
        buf.writeDouble(this.zs);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
        xs = buf.readDouble();
        ys = buf.readDouble();
        zs = buf.readDouble();
    }


    public static class Handler implements IMessageHandler<PacketRageParticles, IMessage> {

        public IMessage onMessage(PacketRageParticles message, MessageContext ctx) {
            EntityPlayerSP player = Minecraft.getMinecraft().player;

            player.world.spawnParticle(EnumParticleTypes.VILLAGER_ANGRY, true, message.x, message.y, message.z, message.xs, message.ys, message.zs);

            return null;
        }
    }

}