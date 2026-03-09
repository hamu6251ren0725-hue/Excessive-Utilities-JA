package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block.entity.MagicalSnowGlobeBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class MagicalSnowGlobeBlock : Block(Properties.ofFullCopy(Blocks.OAK_PLANKS)), EntityBlock {

	override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
		return SHAPE
	}

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return MagicalSnowGlobeBlockEntity(pos, state)
	}

	companion object {
		val SHAPE: VoxelShape = box(2.0, 0.0, 2.0, 14.0, 12.0, 14.0)
	}

}