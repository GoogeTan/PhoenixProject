package projectend.entity.unit02;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class EntityUterusSegment extends MultiPartEntityPart
{
    private final EntityUterus uterus;
    private final int segment;
    private int deathCounter;

    public EntityUterusSegment(EntityUterus myUterus, int segNum) {
        super(myUterus, "segment" + segNum, 0, 0);
        this.uterus = myUterus;
        this.segment = segNum;
        this.stepHeight = 2;
        deactivate();
        this.setNoGravity(false);
    }

    @Override
    public boolean attackEntityFrom(DamageSource src, float damage) {
        return super.attackEntityFrom(src, damage * 2F / 3F);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        ++this.ticksExisted;

        if (!isInvisible())
            collideWithOthers();

        if (deathCounter > 0)
        {
            deathCounter--;
            if (deathCounter <= 0)
            {
                for (int k = 0; k < 20; k++)
                {
                    double d = rand.nextGaussian() * 0.02D;
                    double d1 = rand.nextGaussian() * 0.02D;
                    double d2 = rand.nextGaussian() * 0.02D;
                    EnumParticleTypes explosionType = rand.nextBoolean() ? EnumParticleTypes.EXPLOSION_LARGE : EnumParticleTypes.EXPLOSION_NORMAL;

                    this.world.spawnParticle(explosionType, (posX + rand.nextFloat() * width * 2.0F) - width, posY + rand.nextFloat() * height, (posZ + rand.nextFloat() * width * 2.0F) - width, d, d1, d2);
                }

                deactivate();
            }
        }
    }

    private void collideWithOthers() {
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(0.2D, 0.0D, 0.2D));

        for (Entity entity : list) {
            if (entity.canBePushed()) {
                this.collideWithEntity(entity);
            }
        }
    }

    private void collideWithEntity(Entity entity) {
        entity.applyEntityCollision(this);

        // attack anything that's not us
        if ((entity instanceof EntityLivingBase) && !(entity instanceof EntityUterus) && !(entity instanceof EntityUterusSegment)) {
            int attackStrength = 2;

            // get rid of nearby deer & look impressive
            if (entity instanceof EntityAnimal) {
                attackStrength *= 3;
            }

            entity.attackEntityFrom(DamageSource.causeMobDamage(uterus), attackStrength);
        }
    }

    public void deactivate() {
        setSize(0, 0);
        setInvisible(true);
    }

    public void activate() {
        setSize(0.1F, 0.1F);
        setInvisible(false);
    }

    // make public
    @Override
    public void setRotation(float yaw, float pitch) {
        super.setRotation(yaw, pitch);
    }

    @Override
    protected void playStepSound(BlockPos pos, Block block) {}

    public void selfDestruct() {
        this.deathCounter = 10;
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }
}
