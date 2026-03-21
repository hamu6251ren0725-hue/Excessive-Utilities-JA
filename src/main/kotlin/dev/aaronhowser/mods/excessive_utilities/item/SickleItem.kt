package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.item.Tiers
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class SickleItem(tier: Tier, properties: Properties) : DiggerItem(tier, ModBlockTagsProvider.SICKLE_MINEABLE, properties) {

	override fun mineBlock(
		stack: ItemStack,
		level: Level,
		state: BlockState,
		pos: BlockPos,
		miningEntity: LivingEntity
	): Boolean {
		if (!isCorrectToolForDrops(stack, state)) return false

		if (level.isServerSide) {
			val radius = stack.getOrDefault(ModDataComponents.RADIUS.get(), 1)
			val blocks = BlockPos.betweenClosed(
				pos.offset(-radius, -radius, -radius),
				pos.offset(radius, radius, radius)
			)

			for (block in blocks) {
				val stateThere = level.getBlockState(block)
				if (isCorrectToolForDrops(stack, stateThere)) {
					level.destroyBlock(block, true, miningEntity)

					stack.hurtAndBreak(1, miningEntity, EquipmentSlot.MAINHAND)

					if (stack.isEmpty) {
						return true
					}
				}
			}
		}

		return true
	}

	companion object {
		fun getDefaultProperties(tier: Tier): Properties {
			return Properties()
				.component(
					ModDataComponents.RADIUS.get(),
					when (tier) {
						Tiers.WOOD -> 1
						Tiers.STONE -> 2
						Tiers.GOLD -> 1
						Tiers.IRON -> 3
						Tiers.DIAMOND -> 4
						Tiers.NETHERITE -> 5
						else -> 1
					}
				)
				.attributes(
					createAttributes(tier, 0f, -3f)
				)
		}
	}

}