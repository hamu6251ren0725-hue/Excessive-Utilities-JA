package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block.base.GpDrainBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.GpDrainBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntityType

class EnchanterBlock : GpDrainBlock(Properties.ofFullCopy(Blocks.IRON_BLOCK)) {

	override fun getBlockEntityType(): BlockEntityType<out GpDrainBlockEntity> {
		return ModBlockEntityTypes.ENCHANTER.get()
	}

}