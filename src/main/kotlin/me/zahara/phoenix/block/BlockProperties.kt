package me.zahara.phoenix.block

import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material
import net.minecraft.world.level.material.MaterialColor
import java.util.function.ToIntFunction

inline fun BlockBehaviour.Properties.ifAbsent
    (
        soundType: SoundType? = null,
        explosionResistance: Float? = null,
        destroyTime: Float? = null,
        requiresCorrectToolForDrops: Boolean? = null,
        isRandomlyTicking: Boolean? = null,
        friction: Float? = null,
        speedFactor: Float? = null,
        jumpFactor: Float? = null,
        canOcclude: Boolean? = null,
        isAir: Boolean? = null
    ): BlockBehaviour.Properties
{
    if (this.soundType == SoundType.STONE && soundType != null) this.soundType = soundType
    if (this.explosionResistance == 0.0f && explosionResistance != null) this.explosionResistance = explosionResistance
    if (this.destroyTime == 0.0f && destroyTime != null) this.destroyTime = destroyTime
    if (!this.requiresCorrectToolForDrops && requiresCorrectToolForDrops != null) this.requiresCorrectToolForDrops = requiresCorrectToolForDrops
    if (!this.isRandomlyTicking && isRandomlyTicking != null) this.isRandomlyTicking = isRandomlyTicking
    if (this.friction == 0.6f && friction != null) this.friction = friction
    if (this.speedFactor == 1.0f && speedFactor != null) this.speedFactor = speedFactor
    if (this.jumpFactor == 1.0f && jumpFactor != null) this.jumpFactor = jumpFactor
    if (!this.canOcclude && canOcclude != null) this.canOcclude = canOcclude
    if (!this.isAir && isAir != null) this.isAir = isAir
    return this
}


inline fun create(
    material: Material,
    materialColor: MaterialColor,
    destroyTime: Float? = null,
    explosionResistance: Float? = null,
    hasCollision: Boolean? = null,
    isRandomlyTicking: Boolean? = null,
    lightEmission: ToIntFunction<BlockState>? = null,
    soundType: SoundType? = null,
    friction: Float? = null,
    speedFactor: Float? = null,
    dynamicShape: Boolean? = null,
    canOcclude: Boolean? = null,
    isAir: Boolean? = null,
    requiresCorrectToolForDrops: Boolean? = null
) : BlockBehaviour.Properties
{
    val res = BlockBehaviour.Properties.of(material, materialColor)
    if (destroyTime != null)
        res.destroyTime = destroyTime
    if (explosionResistance != null)
        res.explosionResistance = explosionResistance
    if (hasCollision != null)
        res.hasCollision = hasCollision
    if (isRandomlyTicking != null)
        res.isRandomlyTicking = isRandomlyTicking
    if (lightEmission != null)
        res.lightEmission = lightEmission
    if (soundType != null)
        res.soundType = soundType
    if (friction != null)
        res.friction = friction
    if (speedFactor != null)
        res.speedFactor = speedFactor
    if (dynamicShape != null)
        res.dynamicShape = dynamicShape
    if (canOcclude != null)
        res.canOcclude = canOcclude
    if (isAir != null)
        res.isAir = isAir
    if (requiresCorrectToolForDrops != null)
        res.requiresCorrectToolForDrops = requiresCorrectToolForDrops
    return res
}