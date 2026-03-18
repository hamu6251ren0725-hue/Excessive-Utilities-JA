package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block.base.GpDrainBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.GpDrainBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class EnchanterBlock : GpDrainBlock(Properties.ofFullCopy(Blocks.IRON_BLOCK)) {

	override fun getBlockEntityType(): BlockEntityType<out GpDrainBlockEntity> {
		return ModBlockEntityTypes.ENCHANTER.get()
	}

	override fun useWithoutItem(state: BlockState, level: Level, pos: BlockPos, player: Player, hitResult: BlockHitResult): InteractionResult {
		val be = level.getBlockEntity(pos)
		if (be is MenuProvider) {
			player.openMenu(be)
			return InteractionResult.sidedSuccess(level.isClientSide)
		}

		return InteractionResult.PASS
	}

}