package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.block_walker.BlockWalker
import dev.aaronhowser.mods.aaron.block_walker.WalkType
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext

class DropOfEvilItem(properties: Properties) : Item(properties) {

	override fun useOn(context: UseOnContext): InteractionResult {
		val level = context.level
		val pos = context.clickedPos
		val blockState = level.getBlockState(pos)
		if (!blockState.isBlock(ModBlockTagsProvider.CURSED_EARTH_REPLACEABLE)) return InteractionResult.PASS

		val blockWalker = BlockWalker(
			level = level,
			walkType = WalkType.ALL_SURROUNDING,
			startPos = pos,
			filter = { level, pos, state ->
				state.isBlock(ModBlockTagsProvider.CURSED_EARTH_REPLACEABLE) && level.isEmptyBlock(pos.above())
			},
			maxDistance = 5,
			maxTotalBlocks = 1000,
			onFinished = { walkedBlocks ->
				for (cb in walkedBlocks) {
					val (posBlock, distance) = cb
				}
			})

		blockWalker.start(10000)

		return InteractionResult.SUCCESS
	}

}