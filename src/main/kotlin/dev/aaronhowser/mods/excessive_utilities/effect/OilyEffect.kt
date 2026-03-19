package dev.aaronhowser.mods.excessive_utilities.effect

import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3

class OilyEffect : MobEffect(
	MobEffectCategory.HARMFUL,
	0x000000
) {

	override fun shouldApplyEffectTickThisTick(duration: Int, amplifier: Int): Boolean {
		return true
	}

	override fun applyEffectTick(livingEntity: LivingEntity, amplifier: Int): Boolean {
		if (livingEntity is Player && !livingEntity.abilities.flying) {
			if (livingEntity.level().isRainingAt(livingEntity.blockPosition())) {
				livingEntity.addDeltaMovement(
					Vec3(
						0.0,
						livingEntity.gravity * 1.1,
						0.0
					)
				)
			}
		}

		return true
	}

}