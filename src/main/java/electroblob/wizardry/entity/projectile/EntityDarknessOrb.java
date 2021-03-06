package electroblob.wizardry.entity.projectile;

import electroblob.wizardry.Wizardry;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.MagicDamage.DamageType;
import electroblob.wizardry.util.WizardryParticleType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityDarknessOrb extends EntityMagicProjectile
{
    public EntityDarknessOrb(World par1World)
    {
        super(par1World);
    }

    public EntityDarknessOrb(World par1World, EntityLivingBase par2EntityLivingBase)
    {
        super(par1World, par2EntityLivingBase);
    }
    
    public EntityDarknessOrb(World par1World, EntityLivingBase par2EntityLivingBase, float damageMultiplier)
    {
        super(par1World, par2EntityLivingBase, damageMultiplier);
    }

    public EntityDarknessOrb(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
    }
    
    @Override
    protected float getSpeed(){
        return 0.5F;
    }

    @Override
    protected void onImpact(RayTraceResult RayTraceResult)
    {
    	Entity target = RayTraceResult.entityHit;
    	
        if (target != null && !MagicDamage.isEntityImmune(DamageType.WITHER, target))
        {
            float damage = 8 * damageMultiplier;
            
            target.attackEntityFrom(MagicDamage.causeIndirectMagicDamage(this, this.getThrower(), DamageType.WITHER).setProjectile(), damage);
            
            if(target instanceof EntityLivingBase && !MagicDamage.isEntityImmune(DamageType.WITHER, target)) ((EntityLivingBase)target).addPotionEffect(new PotionEffect(MobEffects.WITHER, 150, 1));

            this.playSound(SoundEvents.ENTITY_WITHER_HURT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
        }

        this.setDead();
    }
    
    public void onUpdate(){
    	
    	super.onUpdate();
    	
    	if(worldObj.isRemote){
	    	float brightness = rand.nextFloat()*0.2f;
	    	Wizardry.proxy.spawnParticle(WizardryParticleType.SPARKLE, worldObj, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, 0, 0, 0, 20 + rand.nextInt(10), brightness, 0.0f, brightness);
	    	Wizardry.proxy.spawnParticle(WizardryParticleType.DARK_MAGIC, worldObj, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, 0.0d, 0.0d, 0.0d, 0, 0.1f, 0.0f, 0.0f);
    	}
    	
    	if(this.ticksExisted > 150){
            this.setDead();
    	}
    	
    	// Cancels out the slowdown effect in EntityThrowable
    	this.motionX /= 0.99;
    	this.motionY /= 0.99;
    	this.motionZ /= 0.99;
    }
    
    /**
     * Gets the amount of gravity to apply to the thrown entity with each tick.
     */
    protected float getGravityVelocity()
    {
        return 0.0F;
    }
}
