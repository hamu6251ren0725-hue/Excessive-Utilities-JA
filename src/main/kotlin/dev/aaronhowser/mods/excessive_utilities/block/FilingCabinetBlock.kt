package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.block.entity.FilingCabinetBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class FilingCabinetBlock : Block(Properties.ofFullCopy(Blocks.IRON_BLOCK)), EntityBlock {

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return FilingCabinetBlockEntity(pos, state)
	}

	override fun onRemove(state: BlockState, level: Level, pos: BlockPos, newState: BlockState, movedByPiston: Boolean) {
		if (!state.isBlock(newState.block)) {
			val be = level.getBlockEntity(pos)
			if (be is FilingCabinetBlockEntity) {
				be.dropAllItems()
			}
		}

		super.onRemove(state, level, pos, newState, movedByPiston)
	}

}