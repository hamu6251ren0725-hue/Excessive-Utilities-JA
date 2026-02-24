package dev.aaronhowser.mods.excessive_utilities.entity

import dev.aaronhowser.mods.excessive_utilities.registry.ModEntityTypes
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Level

class MagicalBoomerangEntity(
	entityType: EntityType<out Projectile>,
	level: Level
) : Projectile(entityType, level) {

	constructor(level: Level, entity: LivingEntity) : this(ModEntityTypes.MAGICAL_BOOMERANG.get(), level) {
		owner = entity
		setPos(entity.x, entity.eyeY - 0.1, entity.z)
	}

	override fun defineSynchedData(builder: SynchedEntityData.Builder) {}
	override fun handlePortal() {}

	override fun isInvulnerable(): Boolean = true
	override fun isInvulnerableTo(source: DamageSource): Boolean = true

	override fun isNoGravity(): Boolean = true
	override fun getDefaultGravity(): Double = 0.0

}