package com.keletu.kchantments.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntitySplash extends EntityArrow
{
    public static final DataParameter<Integer> DISPOSE_TIME = EntityDataManager.createKey(EntitySplash.class, DataSerializers.VARINT);
    public static final DataParameter<Float> RANGE = EntityDataManager.createKey(EntitySplash.class, DataSerializers.FLOAT);
    public static final DataParameter<Float> DAMAGE = EntityDataManager.createKey(EntitySplash.class, DataSerializers.FLOAT);
    public static final DataParameter<ItemStack> STACK = EntityDataManager.createKey(EntitySplash.class, DataSerializers.ITEM_STACK);

    public Entity shooter;
    int maxDisposeTime = 15;

    @Override
    public void notifyDataManagerChange(DataParameter<?> key)
    {
        super.notifyDataManagerChange(key);
        if(key == DISPOSE_TIME)
        {
            maxDisposeTime = Math.min(dataManager.get(DISPOSE_TIME) - ticksExisted, 15);
        }
    }

    public EntitySplash(World w)
    {
        super(w);
        this.setDamage(9F);
    }

    public ItemStack getStack(){
        return this.dataManager.get(STACK);
    }

    public void setStack(ItemStack stack){
        this.dataManager.set(STACK, stack);
    }

    public float getRange() {
        return this.dataManager.get(RANGE);
    }

    public void setRange(float range) {
        this.dataManager.set(RANGE, range);
    }

    public EntitySplash(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
        this.shooter = shooter;

        this.motionX *= -0.01F;
        this.motionY *= -0.1F;
        this.motionZ *= -0.01F;
    }

    public void setSplashDamage(float damageIn)
    {
        this.dataManager.set(DAMAGE, damageIn);
    }

    public float getSplashDamage()
    {
        return this.dataManager.get(DAMAGE);
    }

    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        if (!this.isSilent() && soundIn != SoundEvents.ENTITY_ARROW_HIT && soundIn != SoundEvents.ENTITY_ARROW_HIT_PLAYER) {
            this.world.playSound(null, this.posX, this.posY, this.posZ, soundIn, this.getSoundCategory(), volume, pitch);
        }
    }

    public int getBrightnessForRender() {
        return 15728880;
    }

    @Override
    public float getBrightness() {
        return 1.0F;
    }

    public double particleDistSq(double toX, double toY, double toZ) {
        double d0 = posX - toX;
        double d1 = posY - toY;
        double d2 = posZ - toZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public void onUpdate()
    {
        super.onUpdate();
        noClip = true;

        float sqrt = MathHelper.sqrt((float) (this.motionX * this.motionX + this.motionZ * this.motionZ));
        if ((sqrt < 0.1F) && this.ticksExisted > getRange()) {
            this.setDead();
        }
        double d0 = 0;
        double d1 = 0.0D;
        double d2 = 0.01D;
        double x = this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width;
        double y = this.posY + this.rand.nextFloat() * this.height - this.height;
        double z = this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width;
        float f = (this.width + this.height + this.width) * 0.333F + 0.5F;
        if (particleDistSq(x, y, z) < f * f) {
            this.world.spawnParticle(EnumParticleTypes.END_ROD, x, y + 0.5D, z, d0, d1, d2);
        }

        if(this.ticksExisted >= getRange()) //<- loop exit for primal
            this.setDead();
    }

    public boolean hasNoGravity()
    {
        return true;
    }
    @Override
    protected void onHit(RayTraceResult object) {
        if (this.isDead)
            return;

        if (world.isRemote)
            return;

        if (!this.world.isRemote && object.typeOfHit == RayTraceResult.Type.BLOCK) {
            return;
        }

        if (object.typeOfHit == RayTraceResult.Type.ENTITY) {
            Entity e = object.entityHit;
            if (e == shooter)
                return;

            if (e instanceof EntityLivingBase && e != this.shooter && !(e instanceof EntitySplash)) {
                EntityLivingBase elb = (EntityLivingBase) e;

                elb.attackEntityFrom(DamageSource.causeArrowDamage(this, shooter), this.getSplashDamage());

                this.setDead();
            }
        }
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(DISPOSE_TIME, 120);
        dataManager.register(RANGE, 50F);
        dataManager.register(STACK, ItemStack.EMPTY);
        dataManager.register(DAMAGE, 0F);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setFloat("Range", this.getRange());
        compound.setFloat("Damage", this.getSplashDamage());

        dataManager.set(STACK, this.getStack());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setRange(compound.getInteger("Range"));
        this.setSplashDamage(compound.getInteger("Damage"));
        this.setStack(dataManager.get(STACK));
    }

    @Override
    protected ItemStack getArrowStack() {
        return null;
    }
}