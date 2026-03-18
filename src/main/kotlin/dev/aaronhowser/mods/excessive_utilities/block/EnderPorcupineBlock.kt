package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.excessive_utilities.block_entity.EnderPorcupineBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.FluidState

class EnderPorcupineBlock : Block(Properties.ofFullCopy(Blocks.STONE)), EntityBlock {

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return EnderPorcupineBlockEntity(pos, state)
	}

	override fun onDestroyedByPlayer(
		state: BlockState,
		level: Level,
		pos: BlockPos,
		player: Player,
		willHarvest: Boolean,
		fluid: FluidState
	): Boolean {
		if (level.isClientSide) {
			return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid)
		}

		val blockEntity = level.getBlockEntity(pos)
		if (blockEntity !is EnderPorcupineBlockEntity) {
			return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid)
		}

		val linkPos = blockEntity.linkedPosition
		if (linkPos == null || !level.mayInteract(player, linkPos)) {
			return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid)
		}

		val stateThere = level.getBlockState(linkPos)
		return if (stateThere.isAir) {
			super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid)
		} else {
			stateThere.onDestroyedByPlayer(level, linkPos, player, willHarvest, fluid)
		}
	}

	override fun playerWillDestroy(
		level: Level,
		pos: BlockPos,
		state: BlockState,
		player: Player
	): BlockState {
		if (level.isServerSide) {
			val blockEntity = level.getBlockEntity(pos)

			if (blockEntity is EnderPorcupineBlockEntity) {
				val linkPos = blockEntity.linkedPosition
				if (linkPos != null && level.mayInteract(player, linkPos)) {
					val stateThere = level.getBlockState(linkPos)
					val broken = stateThere.block.playerWillDestroy(level, linkPos, stateThere, player)
				}
			}
		}

		return super.playerWillDestroy(level, pos, state, player)
	}

}