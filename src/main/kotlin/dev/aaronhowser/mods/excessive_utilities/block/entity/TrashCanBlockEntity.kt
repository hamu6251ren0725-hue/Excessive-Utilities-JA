package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.ItemStackHandler

class TrashCanBlockEntity(
	pos: BlockPos,
	state: BlockState
) : BlockEntity(ModBlockEntityTypes.ENDER_QUARRY.get(), pos, state) {

	private var filterStack: ItemStack = ItemStack.EMPTY

	private val itemHandler: ItemStackHandler =
		object : ItemStackHandler(28) {
			override fun getStackInSlot(slot: Int): ItemStack {
				return when (slot) {
					FILTER_SLOT -> filterStack
					else -> ItemStack.EMPTY
				}
			}

			override fun setStackInSlot(slot: Int, stack: ItemStack) {
				if (slot == FILTER_SLOT) {
					filterStack = stack.copy()
				}
			}

			override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
				if (stack.isEmpty) return ItemStack.EMPTY
				if (slot == FILTER_SLOT) return stack
				if (!passesFilter(stack)) return stack

				if (!simulate) {
					setChanged()
				}

				return ItemStack.EMPTY
			}

			override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
				return if (slot == FILTER_SLOT) {
					stack.isItem(ModItems.ITEM_FILTER)
				} else {
					passesFilter(stack)
				}
			}

			override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
				return ItemStack.EMPTY
			}
		}

	// TODO: Implement filter logic
	fun passesFilter(stack: ItemStack): Boolean {
		if (filterStack.isEmpty) return true
		return true
	}

	companion object {
		const val FILTER_SLOT = 0
	}

}