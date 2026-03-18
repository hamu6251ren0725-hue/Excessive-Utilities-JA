package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.block.base.GpDrainBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.ContainerContainer
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.GpDrainBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class ResonatorBlock : GpDrainBlock(Properties.ofFullCopy(Blocks.COAL_BLOCK)) {

	override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
		return SHAPE
	}

	override fun getBlockEntityType(): BlockEntityType<out GpDrainBlockEntity> {
		return ModBlockEntityTypes.RESONATOR.get()
	}

	companion object {
		val SHAPE: VoxelShape = box(0.0, 0.0, 0.0, 16.0, 15.0, 16.0)
	}
}