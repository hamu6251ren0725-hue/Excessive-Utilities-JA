package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.excessive_utilities.block.TrashCanBlock
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class TrashCanBlockEntity(
	pos: BlockPos,
	state: BlockState
) : BlockEntity(ModBlockEntityTypes.ENDER_QUARRY.get(), pos, state) {

	var trashCanType: TrashCanBlock.Type? = null

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)


	}

	companion object {
		const val TYPE_TAG = "TrashCanType"
	}

}