package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.misc.ImprovedSimpleContainer
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler

class TrashCanBlockEntity(
	pos: BlockPos,
	state: BlockState
) : BlockEntity(ModBlockEntityTypes.ENDER_QUARRY.get(), pos, state) {

	private val filterContainer = ImprovedSimpleContainer(this, 1)
	val filterStack: ItemStack get() = filterContainer.getItem(FILTER_SLOT)

	private val itemHandler: IItemHandler =
		object : IItemHandler {
			override fun getSlots(): Int = 27
			override fun getStackInSlot(slot: Int): ItemStack = ItemStack.EMPTY
			override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack = ItemStack.EMPTY
			override fun getSlotLimit(slot: Int): Int = 64
			override fun isItemValid(slot: Int, stack: ItemStack): Boolean = true

			override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
				return if (passesFilter(stack)) {
					ItemStack.EMPTY
				} else {
					stack
				}
			}
		}

	fun getItemHandler(direction: Direction?): IItemHandler = itemHandler

	// TODO: Implement filter logic
	fun passesFilter(stack: ItemStack): Boolean {
		if (filterStack.isEmpty) return true
		return true
	}

	companion object {
		const val FILTER_SLOT = 0
	}

}