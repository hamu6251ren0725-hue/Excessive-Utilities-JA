package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.container.ExtractOnlyInvWrapper
import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class CreativeChestBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.CREATIVE_CHEST.get(), pos, blockState) {

	private val container: ImprovedSimpleContainer = ImprovedSimpleContainer(this, 1)
	private val itemHandler: ExtractOnlyInvWrapper =
		object : ExtractOnlyInvWrapper(container) {
			override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
				val stackInSlot = container.getItem(slot)
				return stackInSlot.copy()
			}
		}

}