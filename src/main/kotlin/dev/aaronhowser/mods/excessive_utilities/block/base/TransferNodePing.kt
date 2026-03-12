package dev.aaronhowser.mods.excessive_utilities.block.base

import dev.aaronhowser.mods.excessive_utilities.block.TransferNodeBlock
import dev.aaronhowser.mods.excessive_utilities.block.TransferPipeBlock
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level

// https://github.com/Leclowndu93150/Extra-Utilities/blob/master/src/main/java/com/leclowndu93150/extrautils2/blockentity/transfer/TransferNodePing.java
class TransferNodePing(
	private val homePos: BlockPos,
	private val homePlacedOnDirection: Direction
) {

	var currentPingPos: BlockPos = homePos
		private set

	var movingInDirection: Direction = homePlacedOnDirection.opposite
		private set

	private val forkPositions: MutableList<BlockPos> = mutableListOf()

	fun reset() {
		currentPingPos = homePos
		movingInDirection = homePlacedOnDirection.opposite
		forkPositions.clear()
	}

	fun getNextDirections(level: Level): List<Direction> {
		val stateAtPingPos = level.getBlockState(currentPingPos)
		val block = stateAtPingPos.block

		val directions = Direction.entries.toMutableList()
		directions.remove(movingInDirection.opposite)

		when (block) {
			is TransferPipeBlock -> {
				directions.removeIf { dir ->
					val property = TransferPipeBlock.CONNECTIONS[dir.ordinal]
					stateAtPingPos.getValue(property).allowsTravel
				}
			}

			is TransferNodeBlock -> {
				val placedOnDirection = stateAtPingPos.getValue(TransferNodeBlock.PLACED_ON)
				directions.remove(placedOnDirection)
			}

			else -> {
				reset()
				return emptyList()
			}
		}

		return directions
	}

}