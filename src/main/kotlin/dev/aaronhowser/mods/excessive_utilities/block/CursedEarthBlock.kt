package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.chance
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.IntegerProperty

class CursedEarthBlock : Block(Properties.ofFullCopy(Blocks.GRASS_BLOCK)) {

	init {
		registerDefaultState(
			stateDefinition.any()
				.setValue(DISTANCE, 7)
		)
	}

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
		builder.add(DISTANCE)
	}

	override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, movedByPiston: Boolean) {
		val distance = state.getValue(DISTANCE)
		if (distance <= 1) return

		for (dx in -1..1) for (dy in -1..1) for (dz in -1..1) {
			if (!level.random.chance(0.5)) continue

			val neighborPos = pos.offset(dx, dy, dz)
			if (!level.isEmptyBlock(neighborPos.above())) continue

			val neighborState = level.getBlockState(neighborPos)

			if (neighborState.isBlock(this)) {
				val neighborDistance = neighborState.getValue(DISTANCE)
				if (neighborDistance >= distance) continue
			} else if (!neighborState.isBlock(ModBlockTagsProvider.CURSED_EARTH_REPLACEABLE)) {
				continue
			}

			val newState = defaultBlockState().setValue(DISTANCE, distance - 1)
			level.setBlockAndUpdate(neighborPos, newState)
		}

		if (level is ServerLevel) {
			level.scheduleTick(pos, this, 1)
		}
	}

	override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
		level.scheduleTick(pos, this, 1)

		handleFire(level, pos, random)

	}

	companion object {
		val DISTANCE: IntegerProperty = BlockStateProperties.DISTANCE

		private fun handleFire(level: ServerLevel, pos: BlockPos, random: RandomSource) {
			val stateAbove = level.getBlockState(pos.above())
			if (!stateAbove.isBlock(BlockTags.FIRE)) return

			if (random.chance(0.2)) {
				level.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState())
			}

			val randomNearby = BlockPos.randomInCube(random, 10, pos, 5)
			for (nearbyPos in randomNearby) {
				if (!level.isLoaded(nearbyPos)) continue

				val stateThere = level.getBlockState(nearbyPos)
				if (!stateThere.`is`(ModBlocks.CURSED_EARTH)) continue

				val posAboveThere = nearbyPos.above()
				val stateAboveThere = level.getBlockState(posAboveThere)
				if (stateAboveThere.canBeReplaced()) {
					level.setBlockAndUpdate(posAboveThere, stateAboveThere)
				}
			}

		}
	}

}