package projectend.entity.unit02;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;
import projectend.util.EntityUtil;

public class EntityUterus extends EntityMob implements IEntityMultiPart
{
    public int texture = 1;
    private static final int TICKS_BEFORE_HEALING = 600;
    private static final int MAX_SEGMENTS = 36;
    private static final double DEFAULT_SPEED = 0.5;

    private int currentSegmentCount = 0; // not including head
    private final EntityUterusSegment[] bodySegments = new EntityUterusSegment[MAX_SEGMENTS];
    private int ticksSinceDamaged = 0;

    private float randomMotionSpeed;

    private float randomMotionVecX;
    private float randomMotionVecY;
    private float randomMotionVecZ;

    public EntityUterus(World world)
    {
        super(world);
        this.randomMotionSpeed = 1.0F;
        this.setNoGravity(false);
        this.setSize(0.1f, 0.1f);
        this.stepHeight = 1;
        this.experienceValue = 80;
        this.ignoreFrustumCheck = true;

        for (int i = 0; i < bodySegments.length; i++)
            bodySegments[i] = new EntityUterusSegment(this, i);

    }

    protected void initEntityAI()
    {
        tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, 6F));
        tasks.addTask(3, new EntityAIAttackMelee(this, 1D, true));
        tasks.addTask(7, new EntityUterus.AIMoveRandom(this));
    }

    private float getMaxHealthPerDifficulty()
    {
        switch (world.getDifficulty())
        {
            case EASY:
                return 50;
            default:
            case NORMAL:
                return 70;
            case HARD:
                return 120;
        }
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }


    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (world.isRemote || !ForgeEventFactory.getMobGriefingEvent(world, this)) return;

        AxisAlignedBB bb = this.getEntityBoundingBox();

        int minx = MathHelper.floor(bb.minX - 0.75D);
        int miny = MathHelper.floor(bb.minY + 1.01D);
        int minz = MathHelper.floor(bb.minZ - 0.75D);
        int maxx = MathHelper.floor(bb.maxX + 0.75D);
        int maxy = MathHelper.floor(bb.maxY + 0.0D);
        int maxz = MathHelper.floor(bb.maxZ + 0.75D);

        BlockPos min = new BlockPos(minx, miny, minz);
        BlockPos max = new BlockPos(maxx, maxy, maxz);

        if (world.isAreaLoaded(min, max))
        {
            for (BlockPos pos : BlockPos.getAllInBox(min, max))
            {
                IBlockState state = world.getBlockState(pos);
                if (state.getMaterial() == Material.LEAVES && EntityUtil.canDestroyBlock(world, pos, state, this))
                {
                    world.destroyBlock(pos, true);
                }
            }
        }

        if (!this.world.isRemote)
        {
            this.motionX = (double)(this.randomMotionVecX * this.randomMotionSpeed);
            this.motionY = (double)(this.randomMotionVecY * this.randomMotionSpeed);
            this.motionZ = (double)(this.randomMotionVecZ * this.randomMotionSpeed);
        }
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getMaxHealthPerDifficulty());
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(DEFAULT_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(80.0D);
    }

    /**
     * Determine how many segments, from 2-12, the naga should have, dependent on its current health
     */
    private void setSegmentsPerHealth()
    {
        int oldSegments = this.currentSegmentCount;
        int newSegments = 35;
        this.currentSegmentCount = newSegments;
        if (newSegments < oldSegments)
            for (int i = newSegments; i < oldSegments; i++)
                bodySegments[i].selfDestruct();

        else if (newSegments > oldSegments)
            this.activateBodySegments();
    }

    @Override public boolean canTriggerWalking() {
        return false;
    }
    @Override public boolean isInLava() {
        return false;
    }

    @Override
    public void onUpdate()
    {
        if (deathTime > 0)
        {
            for (int k = 0; k < 5; k++) {
                double d = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                EnumParticleTypes explosionType = rand.nextBoolean() ? EnumParticleTypes.EXPLOSION_HUGE : EnumParticleTypes.EXPLOSION_NORMAL;

                world.spawnParticle(explosionType, (posX + rand.nextFloat() * width * 2.0F) - width, posY + rand.nextFloat() * height, (posZ + rand.nextFloat() * width * 2.0F) - width, d, d1, d2);
            }
        }

        // update health
        this.ticksSinceDamaged++;

        if (!this.world.isRemote && this.ticksSinceDamaged > TICKS_BEFORE_HEALING && this.ticksSinceDamaged % 20 == 0) {
            this.heal(1);
        }

        setSegmentsPerHealth();

        super.onUpdate();

        // update bodySegments parts
        for (EntityUterusSegment segment : bodySegments) {
            this.world.updateEntityWithOptionalForce(segment, true);
        }

        moveSegments();

        if(texture < 6)
        texture++;
        else
        texture = 1;

    }

    @Override public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source != DamageSource.FALL && super.attackEntityFrom(source, amount)) {
            this.ticksSinceDamaged = 0;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity toAttack) {
        if (toAttack instanceof EntityLivingBase && ((EntityLivingBase) toAttack).isActiveItemStackBlocking()) {
            toAttack.addVelocity(motionX * 1.25D, 0.5D, motionZ * 1.25D);
            motionX *= -1.5D;
            motionY += 0.5D;
            motionZ *= -1.5D;
            attackEntityFrom(DamageSource.GENERIC, 4F);
            world.playSound(null, toAttack.getPosition(), SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.PLAYERS, 1.0F, 0.8F + this.world.rand.nextFloat() * 0.4F);
            return false;
        }
        boolean result = super.attackEntityAsMob(toAttack);

        if (result) {
            // charging, apply extra pushback
            toAttack.addVelocity(-MathHelper.sin((rotationYaw * 3.141593F) / 180F) * 2.0F, 0.4F, MathHelper.cos((rotationYaw * 3.141593F) / 180F) * 2.0F);
        }

        return result;
    }

    @Override
    public float getBlockPathWeight(BlockPos pos) {
        if (!this.isWithinHomeDistanceFromPosition(pos)) {
            return Float.MIN_VALUE;
        } else {
            return 0.0F;
        }
    }


    @Override
    public void setDead()
    {
        super.setDead();
        for (EntityUterusSegment seg : bodySegments)
        {
            // must use this instead of setDead
            // since multiparts are not added to the world tick list which is what checks isDead
            this.world.removeEntityDangerously(seg);
        }
    }


    private void activateBodySegments()
    {
        for (int i = 0; i < currentSegmentCount; i++)
        {
            EntityUterusSegment segment = bodySegments[i];
            segment.activate();
            segment.setLocationAndAngles(posX + 0.1 * i, posY + 0.5D, posZ + 0.1 * i, rand.nextFloat() * 360F, 0.0F);
            for (int j = 0; j < 20; j++)
            {
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                double d2 = this.rand.nextGaussian() * 0.02D;
                this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL,
                        segment.posX + (double) (this.rand.nextFloat() * segment.width * 2.0F) - (double) segment.width - d0 * 10.0D,
                        segment.posY + (double) (this.rand.nextFloat() * segment.height) - d1 * 10.0D,
                        segment.posZ + (double) (this.rand.nextFloat() * segment.width * 2.0F) - (double) segment.width - d2 * 10.0D,
                        d0, d1, d2);
            }
        }
    }

    /**
     * Sets the heading (ha ha) of the bodySegments segments
     */
    private void moveSegments()
    {
        for (int i = 0; i < this.bodySegments.length; i++)
        {
            Entity leader = i == 0 ? this : this.bodySegments[i - 1];
            double followX = leader.posX;
            double followY = leader.posY;
            double followZ = leader.posZ;

            // также утяжелите положение так, чтобы сегменты немного выпрямились, а передние выпрямились больше
            float angle = (((leader.rotationYaw + 180) * 3.141593F) / 180F);

            double straightenForce = 0.05D + (1.0 / (float) (i + 1)) * 0.5D;

            double idealX = -MathHelper.sin(angle) * straightenForce;
            double idealZ = MathHelper.cos(angle) * straightenForce;


            Vec3d diff = new Vec3d(bodySegments[i].posX - followX, bodySegments[i].posY - followY, bodySegments[i].posZ - followZ);
            diff = diff.normalize();

            //вес сегментов дрейфует к их идеальному положению
            diff = diff.add(idealX, 0, idealZ).normalize();

            double segment_distance = 0.1D; //растояние между сегментами

            double destX = followX + segment_distance * diff.x;
            double destY = followY + segment_distance * diff.y;
            double destZ = followZ + segment_distance * diff.z;

            bodySegments[i].setPosition(destX, destY, destZ);

            double distance = MathHelper.sqrt(diff.x * diff.x + diff.z * diff.z);

            if (i == 0) {
                // наклонить сегмент рядом с головой вверх к голове
                diff = diff.add(0, -0.15, 0);
            }

            bodySegments[i].setRotation((float) (Math.atan2(diff.z, diff.x) * 180.0D / Math.PI) + 90.0F, -(float) (Math.atan2(diff.y, distance) * 180.0D / Math.PI));
        }
    }

    @Override public World getWorld(){ return world; }

    @Override
    public boolean attackEntityFromPart(MultiPartEntityPart part, DamageSource src, float damage) {
        return attackEntityFrom(src, damage);
    }

    @Override
    public Entity[] getParts() {
        return bodySegments;
    }

    @Override
    public boolean isNonBoss() {
        return true;
    }


    public void setMovementVector(float randomMotionVecXIn, float randomMotionVecYIn, float randomMotionVecZIn)
    {
        this.randomMotionVecX = randomMotionVecXIn;
        this.randomMotionVecY = randomMotionVecYIn;
        this.randomMotionVecZ = randomMotionVecZIn;
    }

    public boolean hasMovementVector()
    {
        return this.randomMotionVecX != 0.0F || this.randomMotionVecY != 0.0F || this.randomMotionVecZ != 0.0F;
    }

    static class AIMoveRandom extends EntityAIBase
    {
        private final EntityUterus squid;

        public AIMoveRandom(EntityUterus p_i45859_1_)
        {
            this.squid = p_i45859_1_;
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            return true;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void updateTask()
        {

            int i = this.squid.getIdleTime();

            if (i > 20)
            {
                this.squid.setMovementVector(0.0F, 0.0F, 0.0F);
            }
            else if (this.squid.getRNG().nextInt(50) == 0 || !this.squid.hasMovementVector())
            {
                float f = this.squid.getRNG().nextFloat() * ((float)Math.PI * 2F);
                float f1 = MathHelper.cos(f) * 0.2F;
                float f2 = -0.1F + this.squid.getRNG().nextFloat() * 0.2F;
                float f3 = MathHelper.sin(f) * 0.2F;
                this.squid.setMovementVector(f1, f2, f3);
            }
        }
    }
}
