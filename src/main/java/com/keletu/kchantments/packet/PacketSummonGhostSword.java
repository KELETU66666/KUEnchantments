package com.keletu.kchantments.packet;

import com.google.common.collect.Multimap;
import com.keletu.kchantments.enchantments.KEnchantmentList;
import com.keletu.kchantments.entities.EntitySplash;
import io.netty.buffer.ByteBuf;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.vecmath.Vector3f;

public class PacketSummonGhostSword implements IMessage {


	public PacketSummonGhostSword() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {

	}

	@Override
	public void toBytes(ByteBuf buf) {

	}

	public static class Handler implements IMessageHandler<PacketSummonGhostSword, IMessage> {

		public IMessage onMessage(PacketSummonGhostSword message, MessageContext ctx) {
			EntityPlayerMP playerServ = ctx.getServerHandler().player;

			spawnGhostSwordEntity(playerServ.getHeldItemMainhand(), playerServ);

			return null;
		}
	}

	public static void spawnGhostSwordEntity(ItemStack stack, EntityPlayer playerEntity) {
		if (playerEntity.getCooldownTracker().hasCooldown(stack.getItem()))
			return;
		if (playerEntity.getHeldItem(EnumHand.MAIN_HAND) != stack)
			return;
		final Multimap<String, AttributeModifier> dmg = stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
		double totalDmg = playerEntity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
		playerEntity.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1, 1);
		EntitySplash shot = new EntitySplash(playerEntity.world, playerEntity);
		Vec3d vector3d = playerEntity.getLook(1.0F);
		Vector3f vector3f = new Vector3f((float) vector3d.x, (float) vector3d.y, (float) vector3d.z);
		shot.setRange(EnchantmentHelper.getEnchantmentLevel(KEnchantmentList.swordSplash, stack) * 25);
		shot.setStack(stack);
		shot.setSplashDamage((float) (totalDmg * 0.5F));
		shot.shoot(vector3f.x, vector3f.y, vector3f.z, 1.0F, 0.5F);
		playerEntity.world.spawnEntity(shot);
		stack.damageItem(1, playerEntity);
		playerEntity.getCooldownTracker().setCooldown(stack.getItem(), 10);
	}
}