package dev.aaronhowser.mods.excessive_utilities.block

import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.phys.BlockHitResult

class RedstoneLanternBlock : Block(Properties.ofFullCopy(Blocks.STONE)) {

	init {
		registerDefaultState(
			stateDefinition.any()
				.setValue(POWER, 0)
		)
	}

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
		builder.add(POWER)
	}

	override fun useWithoutItem(state: BlockState, level: Level, pos: BlockPos, player: Player, hitResult: BlockHitResult): InteractionResult {
		val oldPower = state.getValue(POWER)
		val newPower = if (player.isSecondaryUseActive) {
			if (oldPower == 0) 15 else oldPower - 1
		} else {
			if (oldPower == 15) 0 else oldPower + 1
		}

		level.setBlockAndUpdate(pos, state.setValue(POWER, newPower))
		return InteractionResult.SUCCESS
	}

	companion object {
		val POWER: IntegerProperty = BlockStateProperties.POWER
	}

}