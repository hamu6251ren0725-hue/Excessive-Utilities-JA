package dev.aaronhowser.mods.excessive_utilities.item

import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class BedrockiumIngotItem(properties: Properties) : Item(properties) {

	override fun inventoryTick(
		stack: ItemStack,
		level: Level,
		entity: Entity,
		slotId: Int,
		isSelected: Boolean
	) {
		if (level.isClientSide
			|| entity !is LivingEntity
			|| entity.hasInfiniteMaterials()
			|| level.gameTime % 20L != 0L
		) return

		entity.addEffect(MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 45, 2))
	}

}