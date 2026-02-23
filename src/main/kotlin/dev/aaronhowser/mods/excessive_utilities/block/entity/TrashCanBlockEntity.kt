package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class TrashCanBlockEntity(
	pos: BlockPos,
	state: BlockState
) : BlockEntity(ModBlockEntityTypes.ENDER_QUARRY.get(), pos, state) {
}